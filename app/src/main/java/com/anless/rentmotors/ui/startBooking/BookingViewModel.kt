package com.anless.rentmotors.ui.startBooking

import java.util.*
import java.io.File
import android.util.Log
import javax.inject.Inject
import java.text.SimpleDateFormat
import androidx.lifecycle.ViewModel
import com.anless.rentmotors.models.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.MediatorLiveData
import com.anless.rentmotors.api.ErrorCodes
import com.anless.rentmotors.api.ResultCallback
import com.anless.rentmotors.utils.DateFormatter
import com.anless.rentmotors.api.entities.BookDTO
import com.anless.rentmotors.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import com.anless.rentmotors.api.requests.BookingRequest
import com.anless.rentmotors.repositories.BookRepository
import com.anless.rentmotors.ui.personalInfo.PersonalInfo
import com.anless.rentmotors.ui.searchCars.SearchCarFilter
import com.anless.rentmotors.repositories.StationRepository
import com.anless.rentmotors.repositories.VoucherRepository

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val stationRepository: StationRepository,
    private val bookRepository: BookRepository,
    private val voucherRepository: VoucherRepository
) : ViewModel() {

    val searchCarFilter = MediatorLiveData<SearchCarFilter>()
    val stationPickUp = MutableLiveData<Station>()
    val stationDropOff = MutableLiveData<Station>()
    val dateFrom = MutableLiveData<Calendar?>(null)
    val dateTo = MutableLiveData<Calendar?>(null)
    private var returnOnSameStation = true

    var days = MutableLiveData(3)
    var price = MediatorLiveData<Double>()
    var totalPrice = MediatorLiveData<Double>()
    var limitedMileage = MediatorLiveData<Int>()
    private var currency: String = ""

    val car = MutableLiveData<Car>()
    val selectedExtras = MutableLiveData<List<Extra>>()
    private val listExtras = arrayListOf<Extra>()
    val rentalDateString = MediatorLiveData<String>()

    private var firstPartPersonalInfo: PersonalInfo? = null
    val book = MutableLiveData<BookDTO?>()
    val bookHasCreatedEvent = SingleLiveEvent<Void>()

    val error = SingleLiveEvent<Int>()
    val isLoading = MutableLiveData<Boolean>()
    val voucher = MutableLiveData<File>()
    private lateinit var clientEmail: String

    init {
        //initStationPickUp()

        price.addSource(car) {
            price.postValue(it.price)
        }

        price.addSource(days) {
            price.postValue(car.value?.price ?: 0.0)
        }

        limitedMileage.addSource(car) {
            limitedMileage.postValue(it.mileage)
        }

        limitedMileage.addSource(days) {
            limitedMileage.postValue(car.value?.mileage ?: 0)
        }

        searchCarFilter.addSource(stationPickUp) {
            searchCarFilter.postValue(getSearchCarFilter())
        }

        searchCarFilter.addSource(stationDropOff) {
            searchCarFilter.postValue(getSearchCarFilter())
        }

        searchCarFilter.addSource(dateFrom) {
            searchCarFilter.postValue(getSearchCarFilter())
        }

        searchCarFilter.addSource(dateTo) {
            searchCarFilter.postValue(getSearchCarFilter())
        }

        totalPrice.addSource(price) {
            totalPrice.postValue(getTotalPrice())
        }

        totalPrice.addSource(selectedExtras) {
            totalPrice.postValue(getTotalPrice())
        }

        totalPrice.addSource(car) {
            totalPrice.postValue(getTotalPrice())
        }

        rentalDateString.addSource(dateFrom) {
            rentalDateString.postValue(getRentalDateString())
        }

        rentalDateString.addSource(dateTo) {
            rentalDateString.postValue(getRentalDateString())
        }
    }

    private fun getSearchCarFilter(): SearchCarFilter? {
        val stationPickUp = stationPickUp.value ?: return null
        val stationDropOff = stationDropOff.value ?: return null
        val datePickUp = dateFrom.value ?: return null
        val dateDropOff = dateTo.value ?: return null

        return SearchCarFilter(
            stationPickUp.id,
            stationDropOff.id,
            DateFormatter.formatRentDateToServer(datePickUp),
            DateFormatter.formatRentDateToServer(dateDropOff)
        )
    }

    private fun initDates() {
        val calendarFrom = getMinDate()
        val calendarTo = getMinDate()
        calendarTo.add(Calendar.DAY_OF_YEAR, 3)
        dateFrom.postValue(calendarFrom)
        dateTo.postValue(calendarTo)
    }

    fun initStationPickUp() {
        isLoading.postValue(true)
        stationRepository.getStation(Station.MAIN_STATION_ID, object : ResultCallback<Station?> {
            override fun onDataResult(data: Station?) {
                if (data != null) {
                    setStationPickUp(data)
                    isLoading.postValue(false)
                }
            }

            override fun onError(code: Int) {
                error.postValue(ErrorCodes.getMessageByCode(code))
            }
        })
    }

    fun updateMinDateFrom() {
        dateFrom.postValue(getMinDate())
    }

    private fun getMinDate(): Calendar {
        val timezone = stationPickUp.value?.timezone ?: 0
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MINUTE, 30)
        return correctTimezone(calendar, timezone)
    }

    fun getMinDateToInMillis(stationType: Int): Long {
        val calendar = getMinDate()

        if (stationType == StationType.DROP_OFF_LOCATION) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return calendar.timeInMillis
    }

    private fun correctTimezone(calendar: Calendar, timezone: Int): Calendar {
        val mCurrentTimezone = calendar.timeZone.rawOffset
        val mStationTimezone = timezone * 60 * 60 * 1000

        calendar.timeInMillis = calendar.timeInMillis - mCurrentTimezone + mStationTimezone

        return calendar
    }


    fun setDateFrom(calendar: Calendar) {
        dateFrom.value = calendar
        checkDateTo(calendar.timeInMillis)
    }

    fun setDateTo(calendar: Calendar) {
        dateTo.value = calendar
    }

    private fun checkDateTo(msDateFrom: Long) {
        val msDay = 24 * 60 * 60 * 1000 // day in milliseconds
        dateTo.value?.let {
            val msRentDays = it.timeInMillis - msDateFrom // rent days in milliseconds
            if (msRentDays < msDay) {
                val calendar = getMinDate()
                calendar.timeInMillis = msDateFrom + msDay
                setDateTo(calendar)
            }
        }
    }

    fun setStationPickUp(station: Station) {
        stationPickUp.value = station
        if (returnOnSameStation) {
            stationDropOff.postValue(station)
        }

        if (dateFrom.value == null) {
            initDates()
        }
    }

    fun checkDateFrom() {
        val minDate = getMinDate()
        val currentDateFrom = dateFrom.value ?: Calendar.getInstance()
        if (currentDateFrom.timeInMillis < minDate.timeInMillis) {
            dateFrom.postValue(minDate)
        }
    }

    fun setStationDropOff(station: Station) {
        stationDropOff.postValue(station)
    }

    fun setSameStation(isSame: Boolean) {
        returnOnSameStation = isSame

        if (isSame) {
            if (stationPickUp.value != null) {
                stationDropOff.postValue(stationPickUp.value)
            }
        }
    }

    fun setCar(car: Car) {
        this.car.postValue(car)
        listExtras.clear()
        listExtras.addAll(car.extras)
        selectedExtras.postValue(car.extras)
    }

    fun setDays(days: Int) {
        this.days.postValue(days)
    }

    fun editExtra(extraToEdit: Extra) {
        listExtras.forEachIndexed { index, extra ->
            if (extra.code == extraToEdit.code) {
                var newAmount = if (extraToEdit.amount < extraToEdit.maxCount)
                    extraToEdit.amount + 1 else 0

                if (extra.isChildSeat()) {
                    if (isMaxAmountChildSeats()) {
                        newAmount = 0
                    }
                }

                if (extra.code == ExtraType.WHEEL_INSURANCE) {
                    listExtras.forEach {
                        if (it.isSelected() && it.code == ExtraType.FULL_COVER) {
                            return
                        }
                    }
                }

                if (extra.code == ExtraType.FULL_COVER) {
                    for ((i, extraWheel) in listExtras.withIndex()) {
                        if (extraWheel.isSelected() && extraWheel.code == ExtraType.WHEEL_INSURANCE) {
                            listExtras.removeAt(i)
                            val editedExtra = extraWheel.copy(amount = 0)
                            listExtras.add(i, editedExtra)
                            break
                        }
                    }
                }

                listExtras.removeAt(index)

                val editedExtra = extraToEdit.copy(amount = newAmount)
                listExtras.add(index, editedExtra)
                val newList = arrayListOf<Extra>()
                newList.addAll(listExtras)
                selectedExtras.postValue(newList)
                return
            }
        }
    }

    private fun isMaxAmountChildSeats(): Boolean {
        val maxAmount = 3
        var selectedSeats = 0

        listExtras.forEach { extra ->
            if (extra.isSelected() && extra.isChildSeat()) {
                selectedSeats += extra.amount
            }
        }

        return selectedSeats >= maxAmount
    }

    fun setExtraAddress(extraToEdit: Extra, address: String) {
        Log.d("TAG", "setExtraAddress: $address")
        listExtras.forEachIndexed { index, extra ->
            if (extra.code == extraToEdit.code) {
                listExtras.removeAt(index)
                val editedExtra = extraToEdit.copy(address = address)
                listExtras.add(index, editedExtra)
                val newList = arrayListOf<Extra>()
                newList.addAll(listExtras)
                selectedExtras.postValue(newList)
                return
            }
        }
    }

    private fun getExtrasPrice(): Double {
        var extrasPrice = 0.0
        selectedExtras.value?.let {
            it.forEach { extra ->
                extrasPrice += extra.getTotalPrice()
            }
        }

        return extrasPrice
    }

    private fun getFeesPrice(): Double {
        var feesPrice = 0.0
        car.value?.let {
            it.fees.forEach { fee ->
                feesPrice += fee.price
            }
        }

        return feesPrice
    }

    private fun getTotalPrice(): Double {
        return (price.value ?: 0.0) + getExtrasPrice() + getFeesPrice()
    }

    fun sendData(personalInfo: PersonalInfo) {
        val carId = car.value?.id ?: return

        clientEmail = personalInfo.email

        val clientInfo = BookingRequest.Client(
            personalInfo.first_name,
            personalInfo.last_name,
            personalInfo.patronymic,
            personalInfo.phone,
            personalInfo.email,
            personalInfo.country,
            personalInfo.address,
            DateFormatter.formatDocumentsDateToServer(personalInfo.birthday),
            personalInfo.idClient
        )

        val passport = BookingRequest.Passport(
            personalInfo.passportNumber,
            personalInfo.country,
            personalInfo.passportIssuedBy,
            DateFormatter.formatDocumentsDateToServer(personalInfo.passportIssueDate)
        )

        val driverLicense = BookingRequest.DriverLicense(
            personalInfo.dlNumber,
            DateFormatter.formatDocumentsDateToServer(personalInfo.dlIssueDate)
        )

        val extras = selectedExtras.value?.map {
            BookingRequest.OrderExtra(
                it.code,
                it.amount,
                it.address
            )
        }

        val bookingRequest = BookingRequest(
            carId,
            clientInfo,
            passport,
            driverLicense,
            extras ?: listOf(),
            personalInfo.flight_number,
            personalInfo.comments,
            personalInfo.newClient ?: ClientState.NEW_CLIENT
        )

        isLoading.postValue(true)

        bookRepository.book(bookingRequest, object : ResultCallback<BookDTO> {
            override fun onDataResult(data: BookDTO) {
                book.postValue(data)
                isLoading.postValue(false)
                bookHasCreatedEvent.call()
            }

            override fun onError(code: Int) {
                error.postValue(ErrorCodes.getMessageByCode(code))
                isLoading.postValue(false)
            }
        })
    }

    private fun getRentalDateString(): String {
        val pattern = "EEE, dd MMM, HH:mm"
        val formatter = SimpleDateFormat(pattern, Locale.getDefault())

        return formatter.format(dateFrom.value?.timeInMillis)
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } + " - " + formatter.format(
            dateTo.value?.timeInMillis
        )
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }

    fun loadVoucher() {
        val numReservation = book.value?.res_id ?: ""
        isLoading.postValue(true)

        voucherRepository.loadVoucher(numReservation, clientEmail, object : ResultCallback<File> {
            override fun onDataResult(data: File) {
                voucher.postValue(data)
                isLoading.postValue(false)
            }

            override fun onError(code: Int) {
                error.postValue(ErrorCodes.getMessageByCode(code))
                isLoading.postValue(false)
            }
        })
    }

    fun clearAll() {
        returnOnSameStation = true
        dateFrom.value = null
        dateTo.value = null
        book.value = null
    }

    fun setCurrency(currency: String) {
        this.currency = currency
    }

    fun getCurrency(): String {
        return currency
    }

    fun setPersonalInfo(personalInfo: PersonalInfo) {
        firstPartPersonalInfo = personalInfo
    }

    fun getPersonalInfo(): PersonalInfo? {
        return firstPartPersonalInfo
    }
}