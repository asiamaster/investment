package com.dili.uap.rpc;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.POST;
import com.dili.ss.retrofitful.annotation.Restful;
import com.dili.ss.retrofitful.annotation.VOBody;
import com.dili.uap.domain.dto.PaymentRecord;

/**
 * Created by asiam on 2018/1/31 0031.
 */
@Restful("http://invest.artist.com:8086/paymentRecordApi")
public interface PaymentRecordRpc {
    @POST("/insert.api")
    BaseOutput<String> insert(@VOBody PaymentRecord paymentRecord);
}