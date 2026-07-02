package com.example.myapplication

import platform.UIKit.UIDevice

actual val isNative: Boolean = (UIDevice.currentDevice.systemVersion
        .substringBefore(".").toIntOrNull() ?: 0) >= 26
