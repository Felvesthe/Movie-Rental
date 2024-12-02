package com.github.felvesthe.movierental.services.payments;

import com.github.felvesthe.movierental.enums.StatusCode;

public interface PaymentStrategy {

    StatusCode processPayment();
}
