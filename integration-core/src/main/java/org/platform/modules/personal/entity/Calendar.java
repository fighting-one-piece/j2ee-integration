package org.platform.modules.personal.entity;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.time.DateUtils;
import org.hibernate.validator.constraints.Length;
import org.platform.modules.abstr.entity.IdAutoEntity;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "T_PLAT_CALENDAR")
@Access(AccessType.FIELD)
public class Calendar extends IdAutoEntity<Long> {

	private static final long serialVersionUID = 1L;

	/**
     * 所属人
     */
    @Column(name = "USER_ID")
    private Long userId;
    /**
     * 标题
     */
    @Column(name = "TITLE")
    @Length(min = 1, max = 200)
    private String title;

    /**
     * 详细信息
     */
    @Column(name = "DETAILS")
    @Length(min = 0, max = 500)
    private String details;

    /**
     * 开始日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @Column(name = "START_DATE")
    private Date startDate;

    /**
     * 持续时间
     */
    @Column(name = "LENGTH")
    private Integer length;

    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = "HH:mm:ss")
    @Temporal(TemporalType.TIME)
    @Column(name = "START_TIME")
    private Date startTime;

    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "HH:mm:ss")
    @Temporal(TemporalType.TIME)
    @Column(name = "END_TIME")
    private Date endTime;

    @Column(name = "BACKGROUND_COLOR")
    private String backgroundColor;

    @Column(name = "TEXT_COLOR")
    private String textColor;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return DateUtils.addDays(startDate, length - 1);
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }
}
