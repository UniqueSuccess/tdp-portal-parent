package cn.goldencis.tdp.report.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IllegalOperationAlarmDOCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public IllegalOperationAlarmDOCriteria() {
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

        public Criteria andTruenameIsNull() {
            addCriterion("truename is null");
            return (Criteria) this;
        }

        public Criteria andTruenameIsNotNull() {
            addCriterion("truename is not null");
            return (Criteria) this;
        }

        public Criteria andTruenameEqualTo(String value) {
            addCriterion("truename =", value, "truename");
            return (Criteria) this;
        }

        public Criteria andTruenameNotEqualTo(String value) {
            addCriterion("truename <>", value, "truename");
            return (Criteria) this;
        }

        public Criteria andTruenameGreaterThan(String value) {
            addCriterion("truename >", value, "truename");
            return (Criteria) this;
        }

        public Criteria andTruenameGreaterThanOrEqualTo(String value) {
            addCriterion("truename >=", value, "truename");
            return (Criteria) this;
        }

        public Criteria andTruenameLessThan(String value) {
            addCriterion("truename <", value, "truename");
            return (Criteria) this;
        }

        public Criteria andTruenameLessThanOrEqualTo(String value) {
            addCriterion("truename <=", value, "truename");
            return (Criteria) this;
        }

        public Criteria andTruenameLike(String value) {
            addCriterion("truename like", value, "truename");
            return (Criteria) this;
        }

        public Criteria andTruenameNotLike(String value) {
            addCriterion("truename not like", value, "truename");
            return (Criteria) this;
        }

        public Criteria andTruenameIn(List<String> values) {
            addCriterion("truename in", values, "truename");
            return (Criteria) this;
        }

        public Criteria andTruenameNotIn(List<String> values) {
            addCriterion("truename not in", values, "truename");
            return (Criteria) this;
        }

        public Criteria andTruenameBetween(String value1, String value2) {
            addCriterion("truename between", value1, value2, "truename");
            return (Criteria) this;
        }

        public Criteria andTruenameNotBetween(String value1, String value2) {
            addCriterion("truename not between", value1, value2, "truename");
            return (Criteria) this;
        }

        public Criteria andUsernameIsNull() {
            addCriterion("username is null");
            return (Criteria) this;
        }

        public Criteria andUsernameIsNotNull() {
            addCriterion("username is not null");
            return (Criteria) this;
        }

        public Criteria andUsernameEqualTo(String value) {
            addCriterion("username =", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotEqualTo(String value) {
            addCriterion("username <>", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameGreaterThan(String value) {
            addCriterion("username >", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameGreaterThanOrEqualTo(String value) {
            addCriterion("username >=", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameLessThan(String value) {
            addCriterion("username <", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameLessThanOrEqualTo(String value) {
            addCriterion("username <=", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameLike(String value) {
            addCriterion("username like", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotLike(String value) {
            addCriterion("username not like", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameIn(List<String> values) {
            addCriterion("username in", values, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotIn(List<String> values) {
            addCriterion("username not in", values, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameBetween(String value1, String value2) {
            addCriterion("username between", value1, value2, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotBetween(String value1, String value2) {
            addCriterion("username not between", value1, value2, "username");
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

        public Criteria andDepartmentIdIsNull() {
            addCriterion("department_id is null");
            return (Criteria) this;
        }

        public Criteria andDepartmentIdIsNotNull() {
            addCriterion("department_id is not null");
            return (Criteria) this;
        }

        public Criteria andDepartmentIdEqualTo(Integer value) {
            addCriterion("department_id =", value, "departmentId");
            return (Criteria) this;
        }

        public Criteria andDepartmentIdNotEqualTo(Integer value) {
            addCriterion("department_id <>", value, "departmentId");
            return (Criteria) this;
        }

        public Criteria andDepartmentIdGreaterThan(Integer value) {
            addCriterion("department_id >", value, "departmentId");
            return (Criteria) this;
        }

        public Criteria andDepartmentIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("department_id >=", value, "departmentId");
            return (Criteria) this;
        }

        public Criteria andDepartmentIdLessThan(Integer value) {
            addCriterion("department_id <", value, "departmentId");
            return (Criteria) this;
        }

        public Criteria andDepartmentIdLessThanOrEqualTo(Integer value) {
            addCriterion("department_id <=", value, "departmentId");
            return (Criteria) this;
        }

        public Criteria andDepartmentIdIn(List<Integer> values) {
            addCriterion("department_id in", values, "departmentId");
            return (Criteria) this;
        }

        public Criteria andDepartmentIdNotIn(List<Integer> values) {
            addCriterion("department_id not in", values, "departmentId");
            return (Criteria) this;
        }

        public Criteria andDepartmentIdBetween(Integer value1, Integer value2) {
            addCriterion("department_id between", value1, value2, "departmentId");
            return (Criteria) this;
        }

        public Criteria andDepartmentIdNotBetween(Integer value1, Integer value2) {
            addCriterion("department_id not between", value1, value2, "departmentId");
            return (Criteria) this;
        }

        public Criteria andDepartmentNameIsNull() {
            addCriterion("department_name is null");
            return (Criteria) this;
        }

        public Criteria andDepartmentNameIsNotNull() {
            addCriterion("department_name is not null");
            return (Criteria) this;
        }

        public Criteria andDepartmentNameEqualTo(String value) {
            addCriterion("department_name =", value, "departmentName");
            return (Criteria) this;
        }

        public Criteria andDepartmentNameNotEqualTo(String value) {
            addCriterion("department_name <>", value, "departmentName");
            return (Criteria) this;
        }

        public Criteria andDepartmentNameGreaterThan(String value) {
            addCriterion("department_name >", value, "departmentName");
            return (Criteria) this;
        }

        public Criteria andDepartmentNameGreaterThanOrEqualTo(String value) {
            addCriterion("department_name >=", value, "departmentName");
            return (Criteria) this;
        }

        public Criteria andDepartmentNameLessThan(String value) {
            addCriterion("department_name <", value, "departmentName");
            return (Criteria) this;
        }

        public Criteria andDepartmentNameLessThanOrEqualTo(String value) {
            addCriterion("department_name <=", value, "departmentName");
            return (Criteria) this;
        }

        public Criteria andDepartmentNameLike(String value) {
            addCriterion("department_name like", value, "departmentName");
            return (Criteria) this;
        }

        public Criteria andDepartmentNameNotLike(String value) {
            addCriterion("department_name not like", value, "departmentName");
            return (Criteria) this;
        }

        public Criteria andDepartmentNameIn(List<String> values) {
            addCriterion("department_name in", values, "departmentName");
            return (Criteria) this;
        }

        public Criteria andDepartmentNameNotIn(List<String> values) {
            addCriterion("department_name not in", values, "departmentName");
            return (Criteria) this;
        }

        public Criteria andDepartmentNameBetween(String value1, String value2) {
            addCriterion("department_name between", value1, value2, "departmentName");
            return (Criteria) this;
        }

        public Criteria andDepartmentNameNotBetween(String value1, String value2) {
            addCriterion("department_name not between", value1, value2, "departmentName");
            return (Criteria) this;
        }

        public Criteria andDevuniqueIsNull() {
            addCriterion("devunique is null");
            return (Criteria) this;
        }

        public Criteria andDevuniqueIsNotNull() {
            addCriterion("devunique is not null");
            return (Criteria) this;
        }

        public Criteria andDevuniqueEqualTo(String value) {
            addCriterion("devunique =", value, "devunique");
            return (Criteria) this;
        }

        public Criteria andDevuniqueNotEqualTo(String value) {
            addCriterion("devunique <>", value, "devunique");
            return (Criteria) this;
        }

        public Criteria andDevuniqueGreaterThan(String value) {
            addCriterion("devunique >", value, "devunique");
            return (Criteria) this;
        }

        public Criteria andDevuniqueGreaterThanOrEqualTo(String value) {
            addCriterion("devunique >=", value, "devunique");
            return (Criteria) this;
        }

        public Criteria andDevuniqueLessThan(String value) {
            addCriterion("devunique <", value, "devunique");
            return (Criteria) this;
        }

        public Criteria andDevuniqueLessThanOrEqualTo(String value) {
            addCriterion("devunique <=", value, "devunique");
            return (Criteria) this;
        }

        public Criteria andDevuniqueLike(String value) {
            addCriterion("devunique like", value, "devunique");
            return (Criteria) this;
        }

        public Criteria andDevuniqueNotLike(String value) {
            addCriterion("devunique not like", value, "devunique");
            return (Criteria) this;
        }

        public Criteria andDevuniqueIn(List<String> values) {
            addCriterion("devunique in", values, "devunique");
            return (Criteria) this;
        }

        public Criteria andDevuniqueNotIn(List<String> values) {
            addCriterion("devunique not in", values, "devunique");
            return (Criteria) this;
        }

        public Criteria andDevuniqueBetween(String value1, String value2) {
            addCriterion("devunique between", value1, value2, "devunique");
            return (Criteria) this;
        }

        public Criteria andDevuniqueNotBetween(String value1, String value2) {
            addCriterion("devunique not between", value1, value2, "devunique");
            return (Criteria) this;
        }

        public Criteria andExtradataIsNull() {
            addCriterion("extradata is null");
            return (Criteria) this;
        }

        public Criteria andExtradataIsNotNull() {
            addCriterion("extradata is not null");
            return (Criteria) this;
        }

        public Criteria andExtradataEqualTo(String value) {
            addCriterion("extradata =", value, "extradata");
            return (Criteria) this;
        }

        public Criteria andExtradataNotEqualTo(String value) {
            addCriterion("extradata <>", value, "extradata");
            return (Criteria) this;
        }

        public Criteria andExtradataGreaterThan(String value) {
            addCriterion("extradata >", value, "extradata");
            return (Criteria) this;
        }

        public Criteria andExtradataGreaterThanOrEqualTo(String value) {
            addCriterion("extradata >=", value, "extradata");
            return (Criteria) this;
        }

        public Criteria andExtradataLessThan(String value) {
            addCriterion("extradata <", value, "extradata");
            return (Criteria) this;
        }

        public Criteria andExtradataLessThanOrEqualTo(String value) {
            addCriterion("extradata <=", value, "extradata");
            return (Criteria) this;
        }

        public Criteria andExtradataLike(String value) {
            addCriterion("extradata like", value, "extradata");
            return (Criteria) this;
        }

        public Criteria andExtradataNotLike(String value) {
            addCriterion("extradata not like", value, "extradata");
            return (Criteria) this;
        }

        public Criteria andExtradataIn(List<String> values) {
            addCriterion("extradata in", values, "extradata");
            return (Criteria) this;
        }

        public Criteria andExtradataNotIn(List<String> values) {
            addCriterion("extradata not in", values, "extradata");
            return (Criteria) this;
        }

        public Criteria andExtradataBetween(String value1, String value2) {
            addCriterion("extradata between", value1, value2, "extradata");
            return (Criteria) this;
        }

        public Criteria andExtradataNotBetween(String value1, String value2) {
            addCriterion("extradata not between", value1, value2, "extradata");
            return (Criteria) this;
        }

        public Criteria andIpIsNull() {
            addCriterion("ip is null");
            return (Criteria) this;
        }

        public Criteria andIpIsNotNull() {
            addCriterion("ip is not null");
            return (Criteria) this;
        }

        public Criteria andIpEqualTo(String value) {
            addCriterion("ip =", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotEqualTo(String value) {
            addCriterion("ip <>", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpGreaterThan(String value) {
            addCriterion("ip >", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpGreaterThanOrEqualTo(String value) {
            addCriterion("ip >=", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpLessThan(String value) {
            addCriterion("ip <", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpLessThanOrEqualTo(String value) {
            addCriterion("ip <=", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpLike(String value) {
            addCriterion("ip like", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotLike(String value) {
            addCriterion("ip not like", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpIn(List<String> values) {
            addCriterion("ip in", values, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotIn(List<String> values) {
            addCriterion("ip not in", values, "ip");
            return (Criteria) this;
        }

        public Criteria andIpBetween(String value1, String value2) {
            addCriterion("ip between", value1, value2, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotBetween(String value1, String value2) {
            addCriterion("ip not between", value1, value2, "ip");
            return (Criteria) this;
        }

        public Criteria andReceiverIsNull() {
            addCriterion("receiver is null");
            return (Criteria) this;
        }

        public Criteria andReceiverIsNotNull() {
            addCriterion("receiver is not null");
            return (Criteria) this;
        }

        public Criteria andReceiverEqualTo(String value) {
            addCriterion("receiver =", value, "receiver");
            return (Criteria) this;
        }

        public Criteria andReceiverNotEqualTo(String value) {
            addCriterion("receiver <>", value, "receiver");
            return (Criteria) this;
        }

        public Criteria andReceiverGreaterThan(String value) {
            addCriterion("receiver >", value, "receiver");
            return (Criteria) this;
        }

        public Criteria andReceiverGreaterThanOrEqualTo(String value) {
            addCriterion("receiver >=", value, "receiver");
            return (Criteria) this;
        }

        public Criteria andReceiverLessThan(String value) {
            addCriterion("receiver <", value, "receiver");
            return (Criteria) this;
        }

        public Criteria andReceiverLessThanOrEqualTo(String value) {
            addCriterion("receiver <=", value, "receiver");
            return (Criteria) this;
        }

        public Criteria andReceiverLike(String value) {
            addCriterion("receiver like", value, "receiver");
            return (Criteria) this;
        }

        public Criteria andReceiverNotLike(String value) {
            addCriterion("receiver not like", value, "receiver");
            return (Criteria) this;
        }

        public Criteria andReceiverIn(List<String> values) {
            addCriterion("receiver in", values, "receiver");
            return (Criteria) this;
        }

        public Criteria andReceiverNotIn(List<String> values) {
            addCriterion("receiver not in", values, "receiver");
            return (Criteria) this;
        }

        public Criteria andReceiverBetween(String value1, String value2) {
            addCriterion("receiver between", value1, value2, "receiver");
            return (Criteria) this;
        }

        public Criteria andReceiverNotBetween(String value1, String value2) {
            addCriterion("receiver not between", value1, value2, "receiver");
            return (Criteria) this;
        }

        public Criteria andFileNameIsNull() {
            addCriterion("file_name is null");
            return (Criteria) this;
        }

        public Criteria andFileNameIsNotNull() {
            addCriterion("file_name is not null");
            return (Criteria) this;
        }

        public Criteria andFileNameEqualTo(String value) {
            addCriterion("file_name =", value, "fileName");
            return (Criteria) this;
        }

        public Criteria andFileNameNotEqualTo(String value) {
            addCriterion("file_name <>", value, "fileName");
            return (Criteria) this;
        }

        public Criteria andFileNameGreaterThan(String value) {
            addCriterion("file_name >", value, "fileName");
            return (Criteria) this;
        }

        public Criteria andFileNameGreaterThanOrEqualTo(String value) {
            addCriterion("file_name >=", value, "fileName");
            return (Criteria) this;
        }

        public Criteria andFileNameLessThan(String value) {
            addCriterion("file_name <", value, "fileName");
            return (Criteria) this;
        }

        public Criteria andFileNameLessThanOrEqualTo(String value) {
            addCriterion("file_name <=", value, "fileName");
            return (Criteria) this;
        }

        public Criteria andFileNameLike(String value) {
            addCriterion("file_name like", value, "fileName");
            return (Criteria) this;
        }

        public Criteria andFileNameNotLike(String value) {
            addCriterion("file_name not like", value, "fileName");
            return (Criteria) this;
        }

        public Criteria andFileNameIn(List<String> values) {
            addCriterion("file_name in", values, "fileName");
            return (Criteria) this;
        }

        public Criteria andFileNameNotIn(List<String> values) {
            addCriterion("file_name not in", values, "fileName");
            return (Criteria) this;
        }

        public Criteria andFileNameBetween(String value1, String value2) {
            addCriterion("file_name between", value1, value2, "fileName");
            return (Criteria) this;
        }

        public Criteria andFileNameNotBetween(String value1, String value2) {
            addCriterion("file_name not between", value1, value2, "fileName");
            return (Criteria) this;
        }

        public Criteria andHasReadIsNull() {
            addCriterion("has_read is null");
            return (Criteria) this;
        }

        public Criteria andHasReadIsNotNull() {
            addCriterion("has_read is not null");
            return (Criteria) this;
        }

        public Criteria andHasReadEqualTo(Integer value) {
            addCriterion("has_read =", value, "hasRead");
            return (Criteria) this;
        }

        public Criteria andHasReadNotEqualTo(Integer value) {
            addCriterion("has_read <>", value, "hasRead");
            return (Criteria) this;
        }

        public Criteria andHasReadGreaterThan(Integer value) {
            addCriterion("has_read >", value, "hasRead");
            return (Criteria) this;
        }

        public Criteria andHasReadGreaterThanOrEqualTo(Integer value) {
            addCriterion("has_read >=", value, "hasRead");
            return (Criteria) this;
        }

        public Criteria andHasReadLessThan(Integer value) {
            addCriterion("has_read <", value, "hasRead");
            return (Criteria) this;
        }

        public Criteria andHasReadLessThanOrEqualTo(Integer value) {
            addCriterion("has_read <=", value, "hasRead");
            return (Criteria) this;
        }

        public Criteria andHasReadIn(List<Integer> values) {
            addCriterion("has_read in", values, "hasRead");
            return (Criteria) this;
        }

        public Criteria andHasReadNotIn(List<Integer> values) {
            addCriterion("has_read not in", values, "hasRead");
            return (Criteria) this;
        }

        public Criteria andHasReadBetween(Integer value1, Integer value2) {
            addCriterion("has_read between", value1, value2, "hasRead");
            return (Criteria) this;
        }

        public Criteria andHasReadNotBetween(Integer value1, Integer value2) {
            addCriterion("has_read not between", value1, value2, "hasRead");
            return (Criteria) this;
        }

        public Criteria andWarningTypeIsNull() {
            addCriterion("warning_type is null");
            return (Criteria) this;
        }

        public Criteria andWarningTypeIsNotNull() {
            addCriterion("warning_type is not null");
            return (Criteria) this;
        }

        public Criteria andWarningTypeEqualTo(Integer value) {
            addCriterion("warning_type =", value, "warningType");
            return (Criteria) this;
        }

        public Criteria andWarningTypeNotEqualTo(Integer value) {
            addCriterion("warning_type <>", value, "warningType");
            return (Criteria) this;
        }

        public Criteria andWarningTypeGreaterThan(Integer value) {
            addCriterion("warning_type >", value, "warningType");
            return (Criteria) this;
        }

        public Criteria andWarningTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("warning_type >=", value, "warningType");
            return (Criteria) this;
        }

        public Criteria andWarningTypeLessThan(Integer value) {
            addCriterion("warning_type <", value, "warningType");
            return (Criteria) this;
        }

        public Criteria andWarningTypeLessThanOrEqualTo(Integer value) {
            addCriterion("warning_type <=", value, "warningType");
            return (Criteria) this;
        }

        public Criteria andWarningTypeIn(List<Integer> values) {
            addCriterion("warning_type in", values, "warningType");
            return (Criteria) this;
        }

        public Criteria andWarningTypeNotIn(List<Integer> values) {
            addCriterion("warning_type not in", values, "warningType");
            return (Criteria) this;
        }

        public Criteria andWarningTypeBetween(Integer value1, Integer value2) {
            addCriterion("warning_type between", value1, value2, "warningType");
            return (Criteria) this;
        }

        public Criteria andWarningTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("warning_type not between", value1, value2, "warningType");
            return (Criteria) this;
        }

        public Criteria andWarningTimeIsNull() {
            addCriterion("warning_time is null");
            return (Criteria) this;
        }

        public Criteria andWarningTimeIsNotNull() {
            addCriterion("warning_time is not null");
            return (Criteria) this;
        }

        public Criteria andWarningTimeEqualTo(Date value) {
            addCriterion("warning_time =", value, "warningTime");
            return (Criteria) this;
        }

        public Criteria andWarningTimeNotEqualTo(Date value) {
            addCriterion("warning_time <>", value, "warningTime");
            return (Criteria) this;
        }

        public Criteria andWarningTimeGreaterThan(Date value) {
            addCriterion("warning_time >", value, "warningTime");
            return (Criteria) this;
        }

        public Criteria andWarningTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("warning_time >=", value, "warningTime");
            return (Criteria) this;
        }

        public Criteria andWarningTimeLessThan(Date value) {
            addCriterion("warning_time <", value, "warningTime");
            return (Criteria) this;
        }

        public Criteria andWarningTimeLessThanOrEqualTo(Date value) {
            addCriterion("warning_time <=", value, "warningTime");
            return (Criteria) this;
        }

        public Criteria andWarningTimeIn(List<Date> values) {
            addCriterion("warning_time in", values, "warningTime");
            return (Criteria) this;
        }

        public Criteria andWarningTimeNotIn(List<Date> values) {
            addCriterion("warning_time not in", values, "warningTime");
            return (Criteria) this;
        }

        public Criteria andWarningTimeBetween(Date value1, Date value2) {
            addCriterion("warning_time between", value1, value2, "warningTime");
            return (Criteria) this;
        }

        public Criteria andWarningTimeNotBetween(Date value1, Date value2) {
            addCriterion("warning_time not between", value1, value2, "warningTime");
            return (Criteria) this;
        }

        public Criteria andTruenameLikeInsensitive(String value) {
            addCriterion("upper(truename) like", value.toUpperCase(), "truename");
            return (Criteria) this;
        }

        public Criteria andUsernameLikeInsensitive(String value) {
            addCriterion("upper(username) like", value.toUpperCase(), "username");
            return (Criteria) this;
        }

        public Criteria andUserguidLikeInsensitive(String value) {
            addCriterion("upper(userguid) like", value.toUpperCase(), "userguid");
            return (Criteria) this;
        }

        public Criteria andDepartmentNameLikeInsensitive(String value) {
            addCriterion("upper(department_name) like", value.toUpperCase(), "departmentName");
            return (Criteria) this;
        }

        public Criteria andDevuniqueLikeInsensitive(String value) {
            addCriterion("upper(devunique) like", value.toUpperCase(), "devunique");
            return (Criteria) this;
        }

        public Criteria andExtradataLikeInsensitive(String value) {
            addCriterion("upper(extradata) like", value.toUpperCase(), "extradata");
            return (Criteria) this;
        }

        public Criteria andIpLikeInsensitive(String value) {
            addCriterion("upper(ip) like", value.toUpperCase(), "ip");
            return (Criteria) this;
        }

        public Criteria andReceiverLikeInsensitive(String value) {
            addCriterion("upper(receiver) like", value.toUpperCase(), "receiver");
            return (Criteria) this;
        }

        public Criteria andFileNameLikeInsensitive(String value) {
            addCriterion("upper(file_name) like", value.toUpperCase(), "fileName");
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