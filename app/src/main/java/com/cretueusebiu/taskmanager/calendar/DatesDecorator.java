package com.cretueusebiu.taskmanager.calendar;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class DatesDecorator implements DayViewDecorator {

    private int color;
    private int index = 0;
    private int total = 1;
    private HashSet<CalendarDay> dates;

    public DatesDecorator(int color, Collection<CalendarDay> dates) {
        this.color = color;
        this.dates = new HashSet<>(dates);
    }

    public DatesDecorator(int color, ArrayList<CalendarDay> dates, int index, int total) {
        this.color = color;
        this.dates = new HashSet<>(dates);
        this.index = index;
        this.total = total;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
//        if (total == 1) {
//            view.addSpan(new DotSpan(5, color));
//        } else {
            view.addSpan(new MyDotSpan(5, color, index, total));
        //}
    }
}
