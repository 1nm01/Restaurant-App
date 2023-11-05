package com.anmol.restaurantOrderReceiver.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.anmol.restaurantOrderReceiver.entity.Order;

public class FcmViewModel extends ViewModel {
    public static MutableLiveData<Order> data = new MutableLiveData<>();

    public MutableLiveData<Order> getLiveData(){
        if(data == null)
            return data = new MutableLiveData<>();
        return data;
    }

    public void changeLiveData(Order order){
        data.postValue(order);
    }
}
