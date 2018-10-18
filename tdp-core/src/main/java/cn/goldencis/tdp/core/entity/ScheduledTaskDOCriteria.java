package cn.goldencis.tdp.core.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScheduledTaskDOCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ScheduledTaskDOCriteria() {
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

        public Criteria andGuidIsNull() {
            addCriterion("guid is null");
            return (Criteria) this;
        }

        public Criteria andGuidIsNotNull() {
            addCriterion("guid is not null");
            return (Criteria) this;
        }

        public Criteria andGuidEqualTo(String value) {
            addCriterion("guid =", value, "guid");
            return (Criteria) this;
        }

        public Criteria andGuidNotEqualTo(String value) {
            addCriterion("guid <>", value, "guid");
            return (Criteria) this;
        }

        public Criteria andGuidGreaterThan(String value) {
            addCriterion("guid >", value, "guid");
            return (Criteria) this;
        }

        public Criteria andGuidGreaterThanOrEqualTo(String value) {
            addCriterion("guid >=", value, "guid");
            return (Criteria) this;
        }

        public Criteria andGuidLessThan(String value) {
            addCriterion("guid <", value, "guid");
            return (Criteria) this;
        }

        public Criteria andGuidLessThanOrEqualTo(String value) {
            addCriterion("guid <=", value, "guid");
            return (Criteria) this;
        }

        public Criteria andGuidLike(String value) {
            addCriterion("guid like", value, "guid");
            return (Criteria) this;
        }

        public Criteria andGuidNotLike(String value) {
            addCriterion("guid not like", value, "guid");
            return (Criteria) this;
        }

        public Criteria andGuidIn(List<String> values) {
            addCriterion("guid in", values, "guid");
            return (Criteria) this;
        }

        public Criteria andGuidNotIn(List<String> values) {
            addCriterion("guid not in", values, "guid");
            return (Criteria) this;
        }

        public Criteria andGuidBetween(String value1, String value2) {
            addCriterion("guid between", value1, value2, "guid");
            return (Criteria) this;
        }

        public Criteria andGuidNotBetween(String value1, String value2) {
            addCriterion("guid not between", value1, value2, "guid");
            return (Criteria) this;
        }

        public Criteria andTaskReferenceIsNull() {
            addCriterion("task_reference is null");
            return (Criteria) this;
        }

        public Criteria andTaskReferenceIsNotNull() {
            addCriterion("task_reference is not null");
            return (Criteria) this;
        }

        public Criteria andTaskReferenceEqualTo(String value) {
            addCriterion("task_reference =", value, "taskReference");
            return (Criteria) this;
        }

        public Criteria andTaskReferenceNotEqualTo(String value) {
            addCriterion("task_reference <>", value, "taskReference");
            return (Criteria) this;
        }

        public Criteria andTaskReferenceGreaterThan(String value) {
            addCriterion("task_reference >", value, "taskReference");
            return (Criteria) this;
        }

        public Criteria andTaskReferenceGreaterThanOrEqualTo(String value) {
            addCriterion("task_reference >=", value, "taskReference");
            return (Criteria) this;
        }

        public Criteria andTaskReferenceLessThan(String value) {
            addCriterion("task_reference <", value, "taskReference");
            return (Criteria) this;
        }

        public Criteria andTaskReferenceLessThanOrEqualTo(String value) {
            addCriterion("task_reference <=", value, "taskReference");
            return (Criteria) this;
        }

        public Criteria andTaskReferenceLike(String value) {
            addCriterion("task_reference like", value, "taskReference");
            return (Criteria) this;
        }

        public Criteria andTaskReferenceNotLike(String value) {
            addCriterion("task_reference not like", value, "taskReference");
            return (Criteria) this;
        }

        public Criteria andTaskReferenceIn(List<String> values) {
            addCriterion("task_reference in", values, "taskReference");
            return (Criteria) this;
        }

        public Criteria andTaskReferenceNotIn(List<String> values) {
            addCriterion("task_reference not in", values, "taskReference");
            return (Criteria) this;
        }

        public Criteria andTaskReferenceBetween(String value1, String value2) {
            addCriterion("task_reference between", value1, value2, "taskReference");
            return (Criteria) this;
        }

        public Criteria andTaskReferenceNotBetween(String value1, String value2) {
            addCriterion("task_reference not between", value1, value2, "taskReference");
            return (Criteria) this;
        }

        public Criteria andCornIsNull() {
            addCriterion("corn is null");
            return (Criteria) this;
        }

        public Criteria andCornIsNotNull() {
            addCriterion("corn is not null");
            return (Criteria) this;
        }

        public Criteria andCornEqualTo(String value) {
            addCriterion("corn =", value, "corn");
            return (Criteria) this;
        }

        public Criteria andCornNotEqualTo(String value) {
            addCriterion("corn <>", value, "corn");
            return (Criteria) this;
        }

        public Criteria andCornGreaterThan(String value) {
            addCriterion("corn >", value, "corn");
            return (Criteria) this;
        }

        public Criteria andCornGreaterThanOrEqualTo(String value) {
            addCriterion("corn >=", value, "corn");
            return (Criteria) this;
        }

        public Criteria andCornLessThan(String value) {
            addCriterion("corn <", value, "corn");
            return (Criteria) this;
        }

        public Criteria andCornLessThanOrEqualTo(String value) {
            addCriterion("corn <=", value, "corn");
            return (Criteria) this;
        }

        public Criteria andCornLike(String value) {
            addCriterion("corn like", value, "corn");
            return (Criteria) this;
        }

        public Criteria andCornNotLike(String value) {
            addCriterion("corn not like", value, "corn");
            return (Criteria) this;
        }

        public Criteria andCornIn(List<String> values) {
            addCriterion("corn in", values, "corn");
            return (Criteria) this;
        }

        public Criteria andCornNotIn(List<String> values) {
            addCriterion("corn not in", values, "corn");
            return (Criteria) this;
        }

        public Criteria andCornBetween(String value1, String value2) {
            addCriterion("corn between", value1, value2, "corn");
            return (Criteria) this;
        }

        public Criteria andCornNotBetween(String value1, String value2) {
            addCriterion("corn not between", value1, value2, "corn");
            return (Criteria) this;
        }

        public Criteria andProcessingIsNull() {
            addCriterion("processing is null");
            return (Criteria) this;
        }

        public Criteria andProcessingIsNotNull() {
            addCriterion("processing is not null");
            return (Criteria) this;
        }

        public Criteria andProcessingEqualTo(Integer value) {
            addCriterion("processing =", value, "processing");
            return (Criteria) this;
        }

        public Criteria andProcessingNotEqualTo(Integer value) {
            addCriterion("processing <>", value, "processing");
            return (Criteria) this;
        }

        public Criteria andProcessingGreaterThan(Integer value) {
            addCriterion("processing >", value, "processing");
            return (Criteria) this;
        }

        public Criteria andProcessingGreaterThanOrEqualTo(Integer value) {
            addCriterion("processing >=", value, "processing");
            return (Criteria) this;
        }

        public Criteria andProcessingLessThan(Integer value) {
            addCriterion("processing <", value, "processing");
            return (Criteria) this;
        }

        public Criteria andProcessingLessThanOrEqualTo(Integer value) {
            addCriterion("processing <=", value, "processing");
            return (Criteria) this;
        }

        public Criteria andProcessingIn(List<Integer> values) {
            addCriterion("processing in", values, "processing");
            return (Criteria) this;
        }

        public Criteria andProcessingNotIn(List<Integer> values) {
            addCriterion("processing not in", values, "processing");
            return (Criteria) this;
        }

        public Criteria andProcessingBetween(Integer value1, Integer value2) {
            addCriterion("processing between", value1, value2, "processing");
            return (Criteria) this;
        }

        public Criteria andProcessingNotBetween(Integer value1, Integer value2) {
            addCriterion("processing not between", value1, value2, "processing");
            return (Criteria) this;
        }

        public Criteria andTaskSwitchIsNull() {
            addCriterion("task_switch is null");
            return (Criteria) this;
        }

        public Criteria andTaskSwitchIsNotNull() {
            addCriterion("task_switch is not null");
            return (Criteria) this;
        }

        public Criteria andTaskSwitchEqualTo(Integer value) {
            addCriterion("task_switch =", value, "taskSwitch");
            return (Criteria) this;
        }

        public Criteria andTaskSwitchNotEqualTo(Integer value) {
            addCriterion("task_switch <>", value, "taskSwitch");
            return (Criteria) this;
        }

        public Criteria andTaskSwitchGreaterThan(Integer value) {
            addCriterion("task_switch >", value, "taskSwitch");
            return (Criteria) this;
        }

        public Criteria andTaskSwitchGreaterThanOrEqualTo(Integer value) {
            addCriterion("task_switch >=", value, "taskSwitch");
            return (Criteria) this;
        }

        public Criteria andTaskSwitchLessThan(Integer value) {
            addCriterion("task_switch <", value, "taskSwitch");
            return (Criteria) this;
        }

        public Criteria andTaskSwitchLessThanOrEqualTo(Integer value) {
            addCriterion("task_switch <=", value, "taskSwitch");
            return (Criteria) this;
        }

        public Criteria andTaskSwitchIn(List<Integer> values) {
            addCriterion("task_switch in", values, "taskSwitch");
            return (Criteria) this;
        }

        public Criteria andTaskSwitchNotIn(List<Integer> values) {
            addCriterion("task_switch not in", values, "taskSwitch");
            return (Criteria) this;
        }

        public Criteria andTaskSwitchBetween(Integer value1, Integer value2) {
            addCriterion("task_switch between", value1, value2, "taskSwitch");
            return (Criteria) this;
        }

        public Criteria andTaskSwitchNotBetween(Integer value1, Integer value2) {
            addCriterion("task_switch not between", value1, value2, "taskSwitch");
            return (Criteria) this;
        }

        public Criteria andLastExecutionTimeIsNull() {
            addCriterion("last_execution_time is null");
            return (Criteria) this;
        }

        public Criteria andLastExecutionTimeIsNotNull() {
            addCriterion("last_execution_time is not null");
            return (Criteria) this;
        }

        public Criteria andLastExecutionTimeEqualTo(Date value) {
            addCriterion("last_execution_time =", value, "lastExecutionTime");
            return (Criteria) this;
        }

        public Criteria andLastExecutionTimeNotEqualTo(Date value) {
            addCriterion("last_execution_time <>", value, "lastExecutionTime");
            return (Criteria) this;
        }

        public Criteria andLastExecutionTimeGreaterThan(Date value) {
            addCriterion("last_execution_time >", value, "lastExecutionTime");
            return (Criteria) this;
        }

        public Criteria andLastExecutionTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("last_execution_time >=", value, "lastExecutionTime");
            return (Criteria) this;
        }

        public Criteria andLastExecutionTimeLessThan(Date value) {
            addCriterion("last_execution_time <", value, "lastExecutionTime");
            return (Criteria) this;
        }

        public Criteria andLastExecutionTimeLessThanOrEqualTo(Date value) {
            addCriterion("last_execution_time <=", value, "lastExecutionTime");
            return (Criteria) this;
        }

        public Criteria andLastExecutionTimeIn(List<Date> values) {
            addCriterion("last_execution_time in", values, "lastExecutionTime");
            return (Criteria) this;
        }

        public Criteria andLastExecutionTimeNotIn(List<Date> values) {
            addCriterion("last_execution_time not in", values, "lastExecutionTime");
            return (Criteria) this;
        }

        public Criteria andLastExecutionTimeBetween(Date value1, Date value2) {
            addCriterion("last_execution_time between", value1, value2, "lastExecutionTime");
            return (Criteria) this;
        }

        public Criteria andLastExecutionTimeNotBetween(Date value1, Date value2) {
            addCriterion("last_execution_time not between", value1, value2, "lastExecutionTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andGuidLikeInsensitive(String value) {
            addCriterion("upper(guid) like", value.toUpperCase(), "guid");
            return (Criteria) this;
        }

        public Criteria andTaskReferenceLikeInsensitive(String value) {
            addCriterion("upper(task_reference) like", value.toUpperCase(), "taskReference");
            return (Criteria) this;
        }

        public Criteria andCornLikeInsensitive(String value) {
            addCriterion("upper(corn) like", value.toUpperCase(), "corn");
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