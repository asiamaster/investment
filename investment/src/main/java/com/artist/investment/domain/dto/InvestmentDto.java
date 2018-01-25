package com.artist.investment.domain.dto;

import com.artist.investment.domain.Investment;
import com.dili.ss.domain.annotation.Operator;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * 投资查询DTO
 */
@Table(name = "`investment`")
public interface InvestmentDto extends Investment {

    @FieldDef(label="是否投资中")
    @Transient
    Integer getIsProgressing();
    void setIsProgressing(Integer isProgressing);

    @Column(name = "`start_date`")
    @FieldDef(label="大于等于起始日期")
    @EditMode(editor = FieldEditor.Date, required = false)
    @Operator(Operator.GREAT_EQUAL_THAN)
    Date getGteStartDate();
    void setGteStartDate(Date gteStartDate);

    @Column(name = "`start_date`")
    @FieldDef(label="小于起始日期")
    @EditMode(editor = FieldEditor.Date, required = false)
    @Operator(Operator.LITTLE_THAN)
    Date getLtStartDate();
    void setLtStartDate(Date ltStartDate);

    @Column(name = "`end_date`")
    @FieldDef(label="大于等于结束日期")
    @EditMode(editor = FieldEditor.Date, required = false)
    @Operator(Operator.GREAT_EQUAL_THAN)
    Date getGteEndDate();
    void setGteEndDate(Date gteEndDate);

    @Column(name = "`end_date`")
    @FieldDef(label="小于结束日期")
    @EditMode(editor = FieldEditor.Date, required = false)
    @Operator(Operator.LITTLE_THAN)
    Date getLtEndDate();
    void setLtEndDate(Date ltEndDate);


}