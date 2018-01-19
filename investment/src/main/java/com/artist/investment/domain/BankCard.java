package com.artist.investment.domain;

import com.dili.ss.domain.annotation.Like;
import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import java.util.Date;
import javax.persistence.*;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2018-01-19 14:15:47.
 */
@Table(name = "`bank_card`")
public interface BankCard extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`user_id`")
    @FieldDef(label="用户ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getUserId();

    void setUserId(Long userId);

    @Column(name = "`card_type`")
    @FieldDef(label="银行卡类型")
    @EditMode(editor = FieldEditor.Combo, required = false, params="{\"data\":[{\"text\":\"储蓄卡\",\"value\":1},{\"text\":\"信用卡\",\"value\":2}],\"provider\":\"cardTypeProvider\"}")
    Integer getCardType();

    void setCardType(Integer cardType);

    @Column(name = "`account_name`")
    @FieldDef(label="开户名", maxLength = 20)
    @Like(Like.BOTH)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getAccountName();

    void setAccountName(String accountName);

    @Column(name = "`id_number`")
    @FieldDef(label="身份证号", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    @Like(Like.BOTH)
    String getIdNumber();

    void setIdNumber(String idNumber);

    @Column(name = "`subbranch`")
    @FieldDef(label="所属支行", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    @Like(Like.BOTH)
    String getSubbranch();

    void setSubbranch(String subbranch);

    @Column(name = "`card_number`")
    @FieldDef(label="银行卡号", maxLength = 30)
    @Like(Like.BOTH)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCardNumber();

    void setCardNumber(String cardNumber);

    @Column(name = "`bank`")
    @FieldDef(label="开户行", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getBank();

    void setBank(String bank);

    @Column(name = "`is_default`")
    @FieldDef(label="是否默认")
    @EditMode(editor = FieldEditor.Combo, required = false, params="{\"provider\":\"YesOrNoProvider\"}")
    Integer getIsDefault();

    void setIsDefault(Integer isDefault);

    @Column(name = "`is_depository`")
    @FieldDef(label="是否存管帐户")
    @EditMode(editor = FieldEditor.Combo, required = false, params="{\"provider\":\"YesOrNoProvider\"}")
    Integer getIsDepository();

    void setIsDepository(Integer isDepository);

    @Column(name = "`add_time`")
    @FieldDef(label="添加时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    Date getAddTime();

    void setAddTime(Date addTime);
}