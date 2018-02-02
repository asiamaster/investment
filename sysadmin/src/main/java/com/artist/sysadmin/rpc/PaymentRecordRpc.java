package com.artist.sysadmin.rpc;

import com.artist.sysadmin.domain.dto.PaymentRecord;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.POST;
import com.dili.ss.retrofitful.annotation.Restful;
import com.dili.ss.retrofitful.annotation.VOBody;

/**
 * Created by asiam on 2018/1/31 0031.
 */
@Restful("http://invest.artist.com/paymentRecordApi")
public interface PaymentRecordRpc {
    @POST("/insert")
    BaseOutput<String> insert(@VOBody PaymentRecord paymentRecord);
}