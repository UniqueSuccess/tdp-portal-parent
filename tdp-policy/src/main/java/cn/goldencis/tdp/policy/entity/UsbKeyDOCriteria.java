package cn.goldencis.tdp.policy.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UsbKeyDOCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public UsbKeyDOCriteria() {
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

        public Criteria andNameIsNull() {
            addCriterion("name is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("name is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("name =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("name <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("name >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("name >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("name <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("name <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("name like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("name not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("name in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("name not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("name between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("name not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andKeysnIsNull() {
            addCriterion("keysn is null");
            return (Criteria) this;
        }

        public Criteria andKeysnIsNotNull() {
            addCriterion("keysn is not null");
            return (Criteria) this;
        }

        public Criteria andKeysnEqualTo(String value) {
            addCriterion("keysn =", value, "keysn");
            return (Criteria) this;
        }

        public Criteria andKeysnNotEqualTo(String value) {
            addCriterion("keysn <>", value, "keysn");
            return (Criteria) this;
        }

        public Criteria andKeysnGreaterThan(String value) {
            addCriterion("keysn >", value, "keysn");
            return (Criteria) this;
        }

        public Criteria andKeysnGreaterThanOrEqualTo(String value) {
            addCriterion("keysn >=", value, "keysn");
            return (Criteria) this;
        }

        public Criteria andKeysnLessThan(String value) {
            addCriterion("keysn <", value, "keysn");
            return (Criteria) this;
        }

        public Criteria andKeysnLessThanOrEqualTo(String value) {
            addCriterion("keysn <=", value, "keysn");
            return (Criteria) this;
        }

        public Criteria andKeysnLike(String value) {
            addCriterion("keysn like", value, "keysn");
            return (Criteria) this;
        }

        public Criteria andKeysnNotLike(String value) {
            addCriterion("keysn not like", value, "keysn");
            return (Criteria) this;
        }

        public Criteria andKeysnIn(List<String> values) {
            addCriterion("keysn in", values, "keysn");
            return (Criteria) this;
        }

        public Criteria andKeysnNotIn(List<String> values) {
            addCriterion("keysn not in", values, "keysn");
            return (Criteria) this;
        }

        public Criteria andKeysnBetween(String value1, String value2) {
            addCriterion("keysn between", value1, value2, "keysn");
            return (Criteria) this;
        }

        public Criteria andKeysnNotBetween(String value1, String value2) {
            addCriterion("keysn not between", value1, value2, "keysn");
            return (Criteria) this;
        }

        public Criteria andKeynumIsNull() {
            addCriterion("keynum is null");
            return (Criteria) this;
        }

        public Criteria andKeynumIsNotNull() {
            addCriterion("keynum is not null");
            return (Criteria) this;
        }

        public Criteria andKeynumEqualTo(String value) {
            addCriterion("keynum =", value, "keynum");
            return (Criteria) this;
        }

        public Criteria andKeynumNotEqualTo(String value) {
            addCriterion("keynum <>", value, "keynum");
            return (Criteria) this;
        }

        public Criteria andKeynumGreaterThan(String value) {
            addCriterion("keynum >", value, "keynum");
            return (Criteria) this;
        }

        public Criteria andKeynumGreaterThanOrEqualTo(String value) {
            addCriterion("keynum >=", value, "keynum");
            return (Criteria) this;
        }

        public Criteria andKeynumLessThan(String value) {
            addCriterion("keynum <", value, "keynum");
            return (Criteria) this;
        }

        public Criteria andKeynumLessThanOrEqualTo(String value) {
            addCriterion("keynum <=", value, "keynum");
            return (Criteria) this;
        }

        public Criteria andKeynumLike(String value) {
            addCriterion("keynum like", value, "keynum");
            return (Criteria) this;
        }

        public Criteria andKeynumNotLike(String value) {
            addCriterion("keynum not like", value, "keynum");
            return (Criteria) this;
        }

        public Criteria andKeynumIn(List<String> values) {
            addCriterion("keynum in", values, "keynum");
            return (Criteria) this;
        }

        public Criteria andKeynumNotIn(List<String> values) {
            addCriterion("keynum not in", values, "keynum");
            return (Criteria) this;
        }

        public Criteria andKeynumBetween(String value1, String value2) {
            addCriterion("keynum between", value1, value2, "keynum");
            return (Criteria) this;
        }

        public Criteria andKeynumNotBetween(String value1, String value2) {
            addCriterion("keynum not between", value1, value2, "keynum");
            return (Criteria) this;
        }

        public Criteria andRegtimeIsNull() {
            addCriterion("regtime is null");
            return (Criteria) this;
        }

        public Criteria andRegtimeIsNotNull() {
            addCriterion("regtime is not null");
            return (Criteria) this;
        }

        public Criteria andRegtimeEqualTo(Date value) {
            addCriterion("regtime =", value, "regtime");
            return (Criteria) this;
        }

        public Criteria andRegtimeNotEqualTo(Date value) {
            addCriterion("regtime <>", value, "regtime");
            return (Criteria) this;
        }

        public Criteria andRegtimeGreaterThan(Date value) {
            addCriterion("regtime >", value, "regtime");
            return (Criteria) this;
        }

        public Criteria andRegtimeGreaterThanOrEqualTo(Date value) {
            addCriterion("regtime >=", value, "regtime");
            return (Criteria) this;
        }

        public Criteria andRegtimeLessThan(Date value) {
            addCriterion("regtime <", value, "regtime");
            return (Criteria) this;
        }

        public Criteria andRegtimeLessThanOrEqualTo(Date value) {
            addCriterion("regtime <=", value, "regtime");
            return (Criteria) this;
        }

        public Criteria andRegtimeIn(List<Date> values) {
            addCriterion("regtime in", values, "regtime");
            return (Criteria) this;
        }

        public Criteria andRegtimeNotIn(List<Date> values) {
            addCriterion("regtime not in", values, "regtime");
            return (Criteria) this;
        }

        public Criteria andRegtimeBetween(Date value1, Date value2) {
            addCriterion("regtime between", value1, value2, "regtime");
            return (Criteria) this;
        }

        public Criteria andRegtimeNotBetween(Date value1, Date value2) {
            addCriterion("regtime not between", value1, value2, "regtime");
            return (Criteria) this;
        }

        public Criteria andUserguidIsNull() {
            addCriterion("userguid is null");
            return (Criteria) this;
        }

        public Criteria andUserguidIsNotNull() {
            addCriterion("userguid is not null");
            return (Criteria) this;
        }

        public Criteria andUserguidEqualTo(String value) {
            addCriterion("userguid =", value, "userguid");
            return (Criteria) this;
        }

        public Criteria andUserguidNotEqualTo(String value) {
            addCriterion("userguid <>", value, "userguid");
            return (Criteria) this;
        }

        public Criteria andUserguidGreaterThan(String value) {
            addCriterion("userguid >", value, "userguid");
            return (Criteria) this;
        }

        public Criteria andUserguidGreaterThanOrEqualTo(String value) {
            addCriterion("userguid >=", value, "userguid");
            return (Criteria) this;
        }

        public Criteria andUserguidLessThan(String value) {
            addCriterion("userguid <", value, "userguid");
            return (Criteria) this;
        }

        public Criteria andUserguidLessThanOrEqualTo(String value) {
            addCriterion("userguid <=", value, "userguid");
            return (Criteria) this;
        }

        public Criteria andUserguidLike(String value) {
            addCriterion("userguid like", value, "userguid");
            return (Criteria) this;
        }

        public Criteria andUserguidNotLike(String value) {
            addCriterion("userguid not like", value, "userguid");
            return (Criteria) this;
        }

        public Criteria andUserguidIn(List<String> values) {
            addCriterion("userguid in", values, "userguid");
            return (Criteria) this;
        }

        public Criteria andUserguidNotIn(List<String> values) {
            addCriterion("userguid not in", values, "userguid");
            return (Criteria) this;
        }

        public Criteria andUserguidBetween(String value1, String value2) {
            addCriterion("userguid between", value1, value2, "userguid");
            return (Criteria) this;
        }

        public Criteria andUserguidNotBetween(String value1, String value2) {
            addCriterion("userguid not between", value1, value2, "userguid");
            return (Criteria) this;
        }

        public Criteria andNameLikeInsensitive(String value) {
            addCriterion("upper(name) like", value.toUpperCase(), "name");
            return (Criteria) this;
        }

        public Criteria andKeysnLikeInsensitive(String value) {
            addCriterion("upper(keysn) like", value.toUpperCase(), "keysn");
            return (Criteria) this;
        }

        public Criteria andKeynumLikeInsensitive(String value) {
            addCriterion("upper(keynum) like", value.toUpperCase(), "keynum");
            return (Criteria) this;
        }

        public Criteria andUserguidLikeInsensitive(String value) {
            addCriterion("upper(userguid) like", value.toUpperCase(), "userguid");
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