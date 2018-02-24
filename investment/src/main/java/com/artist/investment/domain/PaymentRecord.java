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
 * This file was generated on 2018-01-30 15:31:20.
 */
@Table(name = "`payment_record`")
public interface PaymentRecord extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="ID")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`user_name`")
    @FieldDef(label="用户", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    @Like(Like.BOTH)
    String getUserName();

    void setUserName(String userName);

    @Column(name = "`platform_name`")
    @FieldDef(label="资金去向", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getPlatformName();

    void setPlatformName(String platformName);

    @Column(name = "`name`")
    @FieldDef(label="名称", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getName();

    void setName(String name);

    @Column(name = "`type`")
    @FieldDef(label="类型")
    @EditMode(editor = FieldEditor.Combo, required = false, params="{\"data\":[{\"text\":\"收入\",\"value\":1},{\"text\":\"支出\",\"value\":2},{\"text\":\"应收\",\"value\":3},{\"text\":\"应付\",\"value\":4}],\"provider\":\"paymentRecordTypePro                               vider\"}")
    Integer getType();

    void setType(Integer type);

    @Column(name = "`adjust_amount`")
    @FieldDef(label="调整金额")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getAdjustAmount();

    void setAdjustAmount(Long adjustAmount);

    @Column(name = "`balance`")
    @FieldDef(label="余额")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getBalance();

    void setBalance(Long balance);

    @Column(name = "`notes`")
    @FieldDef(label="备注信息", maxLength = 250)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getNotes();

    void setNotes(String notes);

    @Column(name = "`created_name`")
    @FieldDef(label="创建人", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCreatedName();

    void setCreatedName(String createdName);

    @Column(name = "`created`")
    @FieldDef(label="创建时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    Date getCreated();

    void setCreated(Date created);
}