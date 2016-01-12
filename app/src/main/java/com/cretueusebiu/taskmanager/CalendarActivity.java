package com.cretueusebiu.taskmanager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;

import com.cretueusebiu.taskmanager.calendar.DatesDecorator;
import com.cretueusebiu.taskmanager.models.Reminder;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CalendarActivity extends AbstractActivity implements OnDateSelectedListener {

    private HashMap<String, Integer> days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initialize();

        initCalendar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    private void initCalendar() {
        MaterialCalendarView calendarView = (MaterialCalendarView) findViewById(R.id.calendarView);

        calendarView.setOnDateChangedListener(this);

        ArrayList<Reminder> reminders = Reminder.all();

        days = new HashMap<>();
        HashMap<String, CalendarDay> dates = new HashMap<>();

        for (Reminder reminder : reminders) {
            CalendarDay day = CalendarDay.from(reminder.getCalendar());
            String key = getDayKey(day.getCalendar());

            dates.put(key, day);

            int count = 1;
            if (days.containsKey(key)) {
                count = days.get(key) + 1;
            }

            days.put(key, count);
        }

        ArrayList<CalendarDay> singleDates = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : days.entrySet()) {
            if (entry.getValue() == 1) {
                singleDates.add(dates.get(entry.getKey()));
            } else {
                for (DayViewDecorator d : getDecorators(entry.getValue(), dates.get(entry.getKey()))) {
                    calendarView.addDecorator(d);
                }
            }
        }

        if (singleDates.size() > 0) {
            calendarView.addDecorator(new DatesDecorator(Color.RED, singleDates));
        }
    }

    private ArrayList<DayViewDecorator> getDecorators(int num, CalendarDay day) {
        ArrayList<DayViewDecorator> decorators = new ArrayList<DayViewDecorator>();
        ArrayList<CalendarDay> dates = new ArrayList<CalendarDay>();
        dates.add(day);

        for (int i = 0; i < num; i++) {
            decorators.add(new DatesDecorator(Color.RED, dates, i, num));
        }

        return decorators;
    }

    private String getDayKey(Calendar c) {
        String key = "";
        key += c.get(Calendar.DAY_OF_MONTH);
        key += c.get(Calendar.MONTH);
        key += c.get(Calendar.YEAR);
        return key;
    }

    @Override
     public void onDateSelected(MaterialCalendarView widget, CalendarDay day, boolean selected) {
        String key = getDayKey(day.getCalendar());

        if (days.containsKey(key)) {
            Intent intent = new Intent(this, RemindersActivity.class);
            intent.putExtra("time", day.getCalendar().getTimeInMillis());
            startActivity(intent);
        }
    }
}
