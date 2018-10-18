package cn.goldencis.tdp.approve.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScrnwatermarkLogCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ScrnwatermarkLogCriteria() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andScrnwatermarkIdIsNull() {
            addCriterion("scrnwatermark_id is null");
            return (Criteria) this;
        }

        public Criteria andScrnwatermarkIdIsNotNull() {
            addCriterion("scrnwatermark_id is not null");
            return (Criteria) this;
        }

        public Criteria andScrnwatermarkIdEqualTo(String value) {
            addCriterion("scrnwatermark_id =", value, "scrnwatermarkId");
            return (Criteria) this;
        }

        public Criteria andScrnwatermarkIdNotEqualTo(String value) {
            addCriterion("scrnwatermark_id <>", value, "scrnwatermarkId");
            return (Criteria) this;
        }

        public Criteria andScrnwatermarkIdGreaterThan(String value) {
            addCriterion("scrnwatermark_id >", value, "scrnwatermarkId");
            return (Criteria) this;
        }

        public Criteria andScrnwatermarkIdGreaterThanOrEqualTo(String value) {
            addCriterion("scrnwatermark_id >=", value, "scrnwatermarkId");
            return (Criteria) this;
        }

        public Criteria andScrnwatermarkIdLessThan(String value) {
            addCriterion("scrnwatermark_id <", value, "scrnwatermarkId");
            return (Criteria) this;
        }

        public Criteria andScrnwatermarkIdLessThanOrEqualTo(String value) {
            addCriterion("scrnwatermark_id <=", value, "scrnwatermarkId");
            return (Criteria) this;
        }

        public Criteria andScrnwatermarkIdLike(String value) {
            addCriterion("scrnwatermark_id like", value, "scrnwatermarkId");
            return (Criteria) this;
        }

        public Criteria andScrnwatermarkIdNotLike(String value) {
            addCriterion("scrnwatermark_id not like", value, "scrnwatermarkId");
            return (Criteria) this;
        }

        public Criteria andScrnwatermarkIdIn(List<String> values) {
            addCriterion("scrnwatermark_id in", values, "scrnwatermarkId");
            return (Criteria) this;
        }

        public Criteria andScrnwatermarkIdNotIn(List<String> values) {
            addCriterion("scrnwatermark_id not in", values, "scrnwatermarkId");
            return (Criteria) this;
        }

        public Criteria andScrnwatermarkIdBetween(String value1, String value2) {
            addCriterion("scrnwatermark_id between", value1, value2, "scrnwatermarkId");
            return (Criteria) this;
        }

        public Criteria andScrnwatermarkIdNotBetween(String value1, String value2) {
            addCriterion("scrnwatermark_id not between", value1, value2, "scrnwatermarkId");
            return (Criteria) this;
        }

        public Criteria andAuthIdIsNull() {
            addCriterion("auth_id is null");
            return (Criteria) this;
        }

        public Criteria andAuthIdIsNotNull() {
            addCriterion("auth_id is not null");
            return (Criteria) this;
        }

        public Criteria andAuthIdEqualTo(String value) {
            addCriterion("auth_id =", value, "authId");
            return (Criteria) this;
        }

        public Criteria andAuthIdNotEqualTo(String value) {
            addCriterion("auth_id <>", value, "authId");
            return (Criteria) this;
        }

        public Criteria andAuthIdGreaterThan(String value) {
            addCriterion("auth_id >", value, "authId");
            return (Criteria) this;
        }

        public Criteria andAuthIdGreaterThanOrEqualTo(String value) {
            addCriterion("auth_id >=", value, "authId");
            return (Criteria) this;
        }

        public Criteria andAuthIdLessThan(String value) {
            addCriterion("auth_id <", value, "authId");
            return (Criteria) this;
        }

        public Criteria andAuthIdLessThanOrEqualTo(String value) {
            addCriterion("auth_id <=", value, "authId");
            return (Criteria) this;
        }

        public Criteria andAuthIdLike(String value) {
            addCriterion("auth_id like", value, "authId");
            return (Criteria) this;
        }

        public Criteria andAuthIdNotLike(String value) {
            addCriterion("auth_id not like", value, "authId");
            return (Criteria) this;
        }

        public Criteria andAuthIdIn(List<String> values) {
            addCriterion("auth_id in", values, "authId");
            return (Criteria) this;
        }

        public Criteria andAuthIdNotIn(List<String> values) {
            addCriterion("auth_id not in", values, "authId");
            return (Criteria) this;
        }

        public Criteria andAuthIdBetween(String value1, String value2) {
            addCriterion("auth_id between", value1, value2, "authId");
            return (Criteria) this;
        }

        public Criteria andAuthIdNotBetween(String value1, String value2) {
            addCriterion("auth_id not between", value1, value2, "authId");
            return (Criteria) this;
        }

        public Criteria andApplicantIdIsNull() {
            addCriterion("applicant_id is null");
            return (Criteria) this;
        }

        public Criteria andApplicantIdIsNotNull() {
            addCriterion("applicant_id is not null");
            return (Criteria) this;
        }

        public Criteria andApplicantIdEqualTo(String value) {
            addCriterion("applicant_id =", value, "applicantId");
            return (Criteria) this;
        }

        public Criteria andApplicantIdNotEqualTo(String value) {
            addCriterion("applicant_id <>", value, "applicantId");
            return (Criteria) this;
        }

        public Criteria andApplicantIdGreaterThan(String value) {
            addCriterion("applicant_id >", value, "applicantId");
            return (Criteria) this;
        }

        public Criteria andApplicantIdGreaterThanOrEqualTo(String value) {
            addCriterion("applicant_id >=", value, "applicantId");
            return (Criteria) this;
        }

        public Criteria andApplicantIdLessThan(String value) {
            addCriterion("applicant_id <", value, "applicantId");
            return (Criteria) this;
        }

        public Criteria andApplicantIdLessThanOrEqualTo(String value) {
            addCriterion("applicant_id <=", value, "applicantId");
            return (Criteria) this;
        }

        public Criteria andApplicantIdLike(String value) {
            addCriterion("applicant_id like", value, "applicantId");
            return (Criteria) this;
        }

        public Criteria andApplicantIdNotLike(String value) {
            addCriterion("applicant_id not like", value, "applicantId");
            return (Criteria) this;
        }

        public Criteria andApplicantIdIn(List<String> values) {
            addCriterion("applicant_id in", values, "applicantId");
            return (Criteria) this;
        }

        public Criteria andApplicantIdNotIn(List<String> values) {
            addCriterion("applicant_id not in", values, "applicantId");
            return (Criteria) this;
        }

        public Criteria andApplicantIdBetween(String value1, String value2) {
            addCriterion("applicant_id between", value1, value2, "applicantId");
            return (Criteria) this;
        }

        public Criteria andApplicantIdNotBetween(String value1, String value2) {
            addCriterion("applicant_id not between", value1, value2, "applicantId");
            return (Criteria) this;
        }

        public Criteria andApplicantNameIsNull() {
            addCriterion("applicant_name is null");
            return (Criteria) this;
        }

        public Criteria andApplicantNameIsNotNull() {
            addCriterion("applicant_name is not null");
            return (Criteria) this;
        }

        public Criteria andApplicantNameEqualTo(String value) {
            addCriterion("applicant_name =", value, "applicantName");
            return (Criteria) this;
        }

        public Criteria andApplicantNameNotEqualTo(String value) {
            addCriterion("applicant_name <>", value, "applicantName");
            return (Criteria) this;
        }

        public Criteria andApplicantNameGreaterThan(String value) {
            addCriterion("applicant_name >", value, "applicantName");
            return (Criteria) this;
        }

        public Criteria andApplicantNameGreaterThanOrEqualTo(String value) {
            addCriterion("applicant_name >=", value, "applicantName");
            return (Criteria) this;
        }

        public Criteria andApplicantNameLessThan(String value) {
            addCriterion("applicant_name <", value, "applicantName");
            return (Criteria) this;
        }

        public Criteria andApplicantNameLessThanOrEqualTo(String value) {
            addCriterion("applicant_name <=", value, "applicantName");
            return (Criteria) this;
        }

        public Criteria andApplicantNameLike(String value) {
            addCriterion("applicant_name like", value, "applicantName");
            return (Criteria) this;
        }

        public Criteria andApplicantNameNotLike(String value) {
            addCriterion("applicant_name not like", value, "applicantName");
            return (Criteria) this;
        }

        public Criteria andApplicantNameIn(List<String> values) {
            addCriterion("applicant_name in", values, "applicantName");
            return (Criteria) this;
        }

        public Criteria andApplicantNameNotIn(List<String> values) {
            addCriterion("applicant_name not in", values, "applicantName");
            return (Criteria) this;
        }

        public Criteria andApplicantNameBetween(String value1, String value2) {
            addCriterion("applicant_name between", value1, value2, "applicantName");
            return (Criteria) this;
        }

        public Criteria andApplicantNameNotBetween(String value1, String value2) {
            addCriterion("applicant_name not between", value1, value2, "applicantName");
            return (Criteria) this;
        }

        public Criteria andApplyInfoIsNull() {
            addCriterion("apply_info is null");
            return (Criteria) this;
        }

        public Criteria andApplyInfoIsNotNull() {
            addCriterion("apply_info is not null");
            return (Criteria) this;
        }

        public Criteria andApplyInfoEqualTo(String value) {
            addCriterion("apply_info =", value, "applyInfo");
            return (Criteria) this;
        }

        public Criteria andApplyInfoNotEqualTo(String value) {
            addCriterion("apply_info <>", value, "applyInfo");
            return (Criteria) this;
        }

        public Criteria andApplyInfoGreaterThan(String value) {
            addCriterion("apply_info >", value, "applyInfo");
            return (Criteria) this;
        }

        public Criteria andApplyInfoGreaterThanOrEqualTo(String value) {
            addCriterion("apply_info >=", value, "applyInfo");
            return (Criteria) this;
        }

        public Criteria andApplyInfoLessThan(String value) {
            addCriterion("apply_info <", value, "applyInfo");
            return (Criteria) this;
        }

        public Criteria andApplyInfoLessThanOrEqualTo(String value) {
            addCriterion("apply_info <=", value, "applyInfo");
            return (Criteria) this;
        }

        public Criteria andApplyInfoLike(String value) {
            addCriterion("apply_info like", value, "applyInfo");
            return (Criteria) this;
        }

        public Criteria andApplyInfoNotLike(String value) {
            addCriterion("apply_info not like", value, "applyInfo");
            return (Criteria) this;
        }

        public Criteria andApplyInfoIn(List<String> values) {
            addCriterion("apply_info in", values, "applyInfo");
            return (Criteria) this;
        }

        public Criteria andApplyInfoNotIn(List<String> values) {
            addCriterion("apply_info not in", values, "applyInfo");
            return (Criteria) this;
        }

        public Criteria andApplyInfoBetween(String value1, String value2) {
            addCriterion("apply_info between", value1, value2, "applyInfo");
            return (Criteria) this;
        }

        public Criteria andApplyInfoNotBetween(String value1, String value2) {
            addCriterion("apply_info not between", value1, value2, "applyInfo");
            return (Criteria) this;
        }

        public Criteria andApplyTimeIsNull() {
            addCriterion("apply_time is null");
            return (Criteria) this;
        }

        public Criteria andApplyTimeIsNotNull() {
            addCriterion("apply_time is not null");
            return (Criteria) this;
        }

        public Criteria andApplyTimeEqualTo(Date value) {
            addCriterion("apply_time =", value, "applyTime");
            return (Criteria) this;
        }

        public Criteria andApplyTimeNotEqualTo(Date value) {
            addCriterion("apply_time <>", value, "applyTime");
            return (Criteria) this;
        }

        public Criteria andApplyTimeGreaterThan(Date value) {
            addCriterion("apply_time >", value, "applyTime");
            return (Criteria) this;
        }

        public Criteria andApplyTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("apply_time >=", value, "applyTime");
            return (Criteria) this;
        }

        public Criteria andApplyTimeLessThan(Date value) {
            addCriterion("apply_time <", value, "applyTime");
            return (Criteria) this;
        }

        public Criteria andApplyTimeLessThanOrEqualTo(Date value) {
            addCriterion("apply_time <=", value, "applyTime");
            return (Criteria) this;
        }

        public Criteria andApplyTimeIn(List<Date> values) {
            addCriterion("apply_time in", values, "applyTime");
            return (Criteria) this;
        }

        public Criteria andApplyTimeNotIn(List<Date> values) {
            addCriterion("apply_time not in", values, "applyTime");
            return (Criteria) this;
        }

        public Criteria andApplyTimeBetween(Date value1, Date value2) {
            addCriterion("apply_time between", value1, value2, "applyTime");
            return (Criteria) this;
        }

        public Criteria andApplyTimeNotBetween(Date value1, Date value2) {
            addCriterion("apply_time not between", value1, value2, "applyTime");
            return (Criteria) this;
        }

        public Criteria andScrnwatermarkIdLikeInsensitive(String value) {
            addCriterion("upper(scrnwatermark_id) like", value.toUpperCase(), "scrnwatermarkId");
            return (Criteria) this;
        }

        public Criteria andAuthIdLikeInsensitive(String value) {
            addCriterion("upper(auth_id) like", value.toUpperCase(), "authId");
            return (Criteria) this;
        }

        public Criteria andApplicantIdLikeInsensitive(String value) {
            addCriterion("upper(applicant_id) like", value.toUpperCase(), "applicantId");
            return (Criteria) this;
        }

        public Criteria andApplicantNameLikeInsensitive(String value) {
            addCriterion("upper(applicant_name) like", value.toUpperCase(), "applicantName");
            return (Criteria) this;
        }

        public Criteria andApplyInfoLikeInsensitive(String value) {
            addCriterion("upper(apply_info) like", value.toUpperCase(), "applyInfo");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}