<?xml version="1.0" encoding="UTF-8"?>
<Report>
    <Name>Bookings</Name>
    <Date>${date}</Date>
    <Bookings>
        <#list bookings as booking>
            <Booking>
                <EventCode>${booking.eventCode}</EventCode>
                <Seats>
                <#list booking.seats as seat>
                    <Seat>${seat}</Seat>
                </#list>
                </Seats>
                <Price>${booking.price}</Price>
                <Time>${booking.time}</Time>
                <Status>${booking.status}</Status>
            </Booking>
        </#list>
    </Bookings>
</Report>