package com.example.application;

import androidx.annotation.NonNull;

public class IoT {
    public int Temperature;
    public int Humidity;
    public boolean FanBtn;

    // constructor is important - without it the application will crash
    public IoT(){
        Temperature = 0;
        Humidity = 0;
        FanBtn = false;
    }
}
