package com.jim.multipos.config.common;

import com.jim.multipos.utils.usb_barcode.BootCompleteService;
import com.jim.multipos.utils.usb_barcode.USBService;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ServiceBuilderModule {
    @ContributesAndroidInjector
    abstract USBService contributeUSBService();

    @ContributesAndroidInjector
    abstract BootCompleteService contributBootCompleteService();
}
