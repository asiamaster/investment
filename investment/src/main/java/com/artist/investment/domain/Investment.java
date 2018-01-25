package com.artist.investment.domain;

import com.dili.ss.domain.annotation.Operator;
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
 * This file was generated on 2018-01-23 09:56:55.
 */
@Table(name = "`investment`")
public interface Investment extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`investor_id`")
    @FieldDef(label="投资人id")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getInvestorId();

    void setInvestorId(Long investorId);

    @Column(name = "`platform_id`")
    @FieldDef(label="投资平台id")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getPlatformId();

    void setPlatformId(Long platformId);

    @Column(name = "`project_name`")
    @FieldDef(label="项目名称", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getProjectName();

    void setProjectName(String projectName);

    @Column(name = "`start_date`")
    @FieldDef(label="起始日期")
    @EditMode(editor = FieldEditor.Date, required = false)
    Date getStartDate();

    void setStartDate(Date startDate);

    @Column(name = "`end_date`")
    @FieldDef(label="结束日期")
    @EditMode(editor = FieldEditor.Date, required = false)
    Date getEndDate();

    void setEndDate(Date endDate);

    @Column(name = "`project_duration`")
    @FieldDef(label="项目期限")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getProjectDuration();

    void setProjectDuration(Integer projectDuration);

    @Column(name = "`project_duration_unit`")
    @FieldDef(label="项目期限单位")
    @EditMode(editor = FieldEditor.Combo, required = false, params="{\"data\":[{\"text\":\"天\",\"value\":1},{\"text\":\"周\",\"value\":2},{\"text\":\"月\",\"value\":3},{\"text\":\"季\",\"value\":4},{\"text\":\"年\",\"value\":5}],\"provider\":\"projectDurationUnitProvider\"}")
    Integer getProjectDurationUnit();

    void setProjectDurationUnit(Integer projectDurationUnit);

    @Column(name = "`profit_ratio`")
    @FieldDef(label="年化收益率")
    @EditMode(editor = FieldEditor.Number, required = false)
    Float getProfitRatio();

    void setProfitRatio(Float profitRatio);

    @Column(name = "`deducted`")
    @FieldDef(label="已抵扣")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getDeducted();

    void setDeducted(Long deducted);

    @Column(name = "`interest_coupon`")
    @FieldDef(label="加息券")
    @EditMode(editor = FieldEditor.Number, required = false)
    Float getInterestCoupon();

    void setInterestCoupon(Float interestCoupon);

    @Column(name = "`cash_coupon`")
    @FieldDef(label="现金券")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getCashCoupon();

    void setCashCoupon(Long cashCoupon);

    @Column(name = "`investment`")
    @FieldDef(label="投资额")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getInvestment();

    void setInvestment(Long investment);

    @Column(name = "`is_remind`")
    @FieldDef(label="是否到期提醒")
    @EditMode(editor = FieldEditor.Combo, required = false, params="{\"data\":[{\"text\":\"否\",\"value\":0},{\"text\":\"是\",\"value\":1}],\"provider\":\"YesOrNoProvider\"}")
    Integer getIsRemind();

    void setIsRemind(Integer isRemind);

    @Column(name = "`bank_card_id`")
    @FieldDef(label="投资卡")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getBankCardId();

    void setBankCardId(Long bankCardId);

    @Column(name = "`notes`")
    @FieldDef(label="备注信息", maxLength = 250)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getNotes();

    void setNotes(String notes);

    @Column(name = "`created_id`")
    @FieldDef(label="创建人")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getCreatedId();

    void setCreatedId(Long createdId);

    @Column(name = "`modified_id`")
    @FieldDef(label="修改人")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getModifiedId();

    void setModifiedId(Long modifiedId);

    @Column(name = "`created`")
    @FieldDef(label="创建时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    Date getCreated();

    void setCreated(Date created);

    @Column(name = "`modified`")
    @FieldDef(label="修改时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    Date getModified();

    void setModified(Date modified);
}