package org.skellig.demo.app.event.handler.event;

import org.skellig.demo.app.event.handler.event.model.EventChangedEvent;
import org.skellig.demo.app.event.handler.event.model.EventRemovedEvent;
import org.skellig.demo.app.rest.model.Event;
import org.skellig.demo.app.service.EventService;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventChangedEventHandler {

    private EventService eventService;

    public EventChangedEventHandler(EventService eventService) {
        this.eventService = eventService;
    }

    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue(name = "event.changed"), exchange = @Exchange(name = "events"))})
    public void receiveEventChanged(EventChangedEvent event) {
        System.out.println(event);
        try {
            eventService.changeEvent(event);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue(name = "event.removed"), exchange = @Exchange(name = "events"))})
    public void receiveEventRemoved(EventRemovedEvent event) {
        System.out.println(event);
        eventService.deleteEvent(event.getCode());
    }
}