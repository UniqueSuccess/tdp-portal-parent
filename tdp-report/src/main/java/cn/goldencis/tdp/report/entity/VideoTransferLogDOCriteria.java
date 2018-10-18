package cn.goldencis.tdp.report.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VideoTransferLogDOCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public VideoTransferLogDOCriteria() {
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

        public Criteria andFilePathIsNull() {
            addCriterion("file_path is null");
            return (Criteria) this;
        }

        public Criteria andFilePathIsNotNull() {
            addCriterion("file_path is not null");
            return (Criteria) this;
        }

        public Criteria andFilePathEqualTo(String value) {
            addCriterion("file_path =", value, "filePath");
            return (Criteria) this;
        }

        public Criteria andFilePathNotEqualTo(String value) {
            addCriterion("file_path <>", value, "filePath");
            return (Criteria) this;
        }

        public Criteria andFilePathGreaterThan(String value) {
            addCriterion("file_path >", value, "filePath");
            return (Criteria) this;
        }

        public Criteria andFilePathGreaterThanOrEqualTo(String value) {
            addCriterion("file_path >=", value, "filePath");
            return (Criteria) this;
        }

        public Criteria andFilePathLessThan(String value) {
            addCriterion("file_path <", value, "filePath");
            return (Criteria) this;
        }

        public Criteria andFilePathLessThanOrEqualTo(String value) {
            addCriterion("file_path <=", value, "filePath");
            return (Criteria) this;
        }

        public Criteria andFilePathLike(String value) {
            addCriterion("file_path like", value, "filePath");
            return (Criteria) this;
        }

        public Criteria andFilePathNotLike(String value) {
            addCriterion("file_path not like", value, "filePath");
            return (Criteria) this;
        }

        public Criteria andFilePathIn(List<String> values) {
            addCriterion("file_path in", values, "filePath");
            return (Criteria) this;
        }

        public Criteria andFilePathNotIn(List<String> values) {
            addCriterion("file_path not in", values, "filePath");
            return (Criteria) this;
        }

        public Criteria andFilePathBetween(String value1, String value2) {
            addCriterion("file_path between", value1, value2, "filePath");
            return (Criteria) this;
        }

        public Criteria andFilePathNotBetween(String value1, String value2) {
            addCriterion("file_path not between", value1, value2, "filePath");
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

        public Criteria andTransferTimeIsNull() {
            addCriterion("transfer_time is null");
            return (Criteria) this;
        }

        public Criteria andTransferTimeIsNotNull() {
            addCriterion("transfer_time is not null");
            return (Criteria) this;
        }

        public Criteria andTransferTimeEqualTo(Date value) {
            addCriterion("transfer_time =", value, "transferTime");
            return (Criteria) this;
        }

        public Criteria andTransferTimeNotEqualTo(Date value) {
            addCriterion("transfer_time <>", value, "transferTime");
            return (Criteria) this;
        }

        public Criteria andTransferTimeGreaterThan(Date value) {
            addCriterion("transfer_time >", value, "transferTime");
            return (Criteria) this;
        }

        public Criteria andTransferTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("transfer_time >=", value, "transferTime");
            return (Criteria) this;
        }

        public Criteria andTransferTimeLessThan(Date value) {
            addCriterion("transfer_time <", value, "transferTime");
            return (Criteria) this;
        }

        public Criteria andTransferTimeLessThanOrEqualTo(Date value) {
            addCriterion("transfer_time <=", value, "transferTime");
            return (Criteria) this;
        }

        public Criteria andTransferTimeIn(List<Date> values) {
            addCriterion("transfer_time in", values, "transferTime");
            return (Criteria) this;
        }

        public Criteria andTransferTimeNotIn(List<Date> values) {
            addCriterion("transfer_time not in", values, "transferTime");
            return (Criteria) this;
        }

        public Criteria andTransferTimeBetween(Date value1, Date value2) {
            addCriterion("transfer_time between", value1, value2, "transferTime");
            return (Criteria) this;
        }

        public Criteria andTransferTimeNotBetween(Date value1, Date value2) {
            addCriterion("transfer_time not between", value1, value2, "transferTime");
            return (Criteria) this;
        }

        public Criteria andTranuniqueIsNull() {
            addCriterion("tranunique is null");
            return (Criteria) this;
        }

        public Criteria andTranuniqueIsNotNull() {
            addCriterion("tranunique is not null");
            return (Criteria) this;
        }

        public Criteria andTranuniqueEqualTo(String value) {
            addCriterion("tranunique =", value, "tranunique");
            return (Criteria) this;
        }

        public Criteria andTranuniqueNotEqualTo(String value) {
            addCriterion("tranunique <>", value, "tranunique");
            return (Criteria) this;
        }

        public Criteria andTranuniqueGreaterThan(String value) {
            addCriterion("tranunique >", value, "tranunique");
            return (Criteria) this;
        }

        public Criteria andTranuniqueGreaterThanOrEqualTo(String value) {
            addCriterion("tranunique >=", value, "tranunique");
            return (Criteria) this;
        }

        public Criteria andTranuniqueLessThan(String value) {
            addCriterion("tranunique <", value, "tranunique");
            return (Criteria) this;
        }

        public Criteria andTranuniqueLessThanOrEqualTo(String value) {
            addCriterion("tranunique <=", value, "tranunique");
            return (Criteria) this;
        }

        public Criteria andTranuniqueLike(String value) {
            addCriterion("tranunique like", value, "tranunique");
            return (Criteria) this;
        }

        public Criteria andTranuniqueNotLike(String value) {
            addCriterion("tranunique not like", value, "tranunique");
            return (Criteria) this;
        }

        public Criteria andTranuniqueIn(List<String> values) {
            addCriterion("tranunique in", values, "tranunique");
            return (Criteria) this;
        }

        public Criteria andTranuniqueNotIn(List<String> values) {
            addCriterion("tranunique not in", values, "tranunique");
            return (Criteria) this;
        }

        public Criteria andTranuniqueBetween(String value1, String value2) {
            addCriterion("tranunique between", value1, value2, "tranunique");
            return (Criteria) this;
        }

        public Criteria andTranuniqueNotBetween(String value1, String value2) {
            addCriterion("tranunique not between", value1, value2, "tranunique");
            return (Criteria) this;
        }

        public Criteria andFftypeIsNull() {
            addCriterion("fftype is null");
            return (Criteria) this;
        }

        public Criteria andFftypeIsNotNull() {
            addCriterion("fftype is not null");
            return (Criteria) this;
        }

        public Criteria andFftypeEqualTo(Integer value) {
            addCriterion("fftype =", value, "fftype");
            return (Criteria) this;
        }

        public Criteria andFftypeNotEqualTo(Integer value) {
            addCriterion("fftype <>", value, "fftype");
            return (Criteria) this;
        }

        public Criteria andFftypeGreaterThan(Integer value) {
            addCriterion("fftype >", value, "fftype");
            return (Criteria) this;
        }

        public Criteria andFftypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("fftype >=", value, "fftype");
            return (Criteria) this;
        }

        public Criteria andFftypeLessThan(Integer value) {
            addCriterion("fftype <", value, "fftype");
            return (Criteria) this;
        }

        public Criteria andFftypeLessThanOrEqualTo(Integer value) {
            addCriterion("fftype <=", value, "fftype");
            return (Criteria) this;
        }

        public Criteria andFftypeIn(List<Integer> values) {
            addCriterion("fftype in", values, "fftype");
            return (Criteria) this;
        }

        public Criteria andFftypeNotIn(List<Integer> values) {
            addCriterion("fftype not in", values, "fftype");
            return (Criteria) this;
        }

        public Criteria andFftypeBetween(Integer value1, Integer value2) {
            addCriterion("fftype between", value1, value2, "fftype");
            return (Criteria) this;
        }

        public Criteria andFftypeNotBetween(Integer value1, Integer value2) {
            addCriterion("fftype not between", value1, value2, "fftype");
            return (Criteria) this;
        }

        public Criteria andNodeLevelIsNull() {
            addCriterion("node_level is null");
            return (Criteria) this;
        }

        public Criteria andNodeLevelIsNotNull() {
            addCriterion("node_level is not null");
            return (Criteria) this;
        }

        public Criteria andNodeLevelEqualTo(Integer value) {
            addCriterion("node_level =", value, "nodeLevel");
            return (Criteria) this;
        }

        public Criteria andNodeLevelNotEqualTo(Integer value) {
            addCriterion("node_level <>", value, "nodeLevel");
            return (Criteria) this;
        }

        public Criteria andNodeLevelGreaterThan(Integer value) {
            addCriterion("node_level >", value, "nodeLevel");
            return (Criteria) this;
        }

        public Criteria andNodeLevelGreaterThanOrEqualTo(Integer value) {
            addCriterion("node_level >=", value, "nodeLevel");
            return (Criteria) this;
        }

        public Criteria andNodeLevelLessThan(Integer value) {
            addCriterion("node_level <", value, "nodeLevel");
            return (Criteria) this;
        }

        public Criteria andNodeLevelLessThanOrEqualTo(Integer value) {
            addCriterion("node_level <=", value, "nodeLevel");
            return (Criteria) this;
        }

        public Criteria andNodeLevelIn(List<Integer> values) {
            addCriterion("node_level in", values, "nodeLevel");
            return (Criteria) this;
        }

        public Criteria andNodeLevelNotIn(List<Integer> values) {
            addCriterion("node_level not in", values, "nodeLevel");
            return (Criteria) this;
        }

        public Criteria andNodeLevelBetween(Integer value1, Integer value2) {
            addCriterion("node_level between", value1, value2, "nodeLevel");
            return (Criteria) this;
        }

        public Criteria andNodeLevelNotBetween(Integer value1, Integer value2) {
            addCriterion("node_level not between", value1, value2, "nodeLevel");
            return (Criteria) this;
        }

        public Criteria andNodeNameIsNull() {
            addCriterion("node_name is null");
            return (Criteria) this;
        }

        public Criteria andNodeNameIsNotNull() {
            addCriterion("node_name is not null");
            return (Criteria) this;
        }

        public Criteria andNodeNameEqualTo(String value) {
            addCriterion("node_name =", value, "nodeName");
            return (Criteria) this;
        }

        public Criteria andNodeNameNotEqualTo(String value) {
            addCriterion("node_name <>", value, "nodeName");
            return (Criteria) this;
        }

        public Criteria andNodeNameGreaterThan(String value) {
            addCriterion("node_name >", value, "nodeName");
            return (Criteria) this;
        }

        public Criteria andNodeNameGreaterThanOrEqualTo(String value) {
            addCriterion("node_name >=", value, "nodeName");
            return (Criteria) this;
        }

        public Criteria andNodeNameLessThan(String value) {
            addCriterion("node_name <", value, "nodeName");
            return (Criteria) this;
        }

        public Criteria andNodeNameLessThanOrEqualTo(String value) {
            addCriterion("node_name <=", value, "nodeName");
            return (Criteria) this;
        }

        public Criteria andNodeNameLike(String value) {
            addCriterion("node_name like", value, "nodeName");
            return (Criteria) this;
        }

        public Criteria andNodeNameNotLike(String value) {
            addCriterion("node_name not like", value, "nodeName");
            return (Criteria) this;
        }

        public Criteria andNodeNameIn(List<String> values) {
            addCriterion("node_name in", values, "nodeName");
            return (Criteria) this;
        }

        public Criteria andNodeNameNotIn(List<String> values) {
            addCriterion("node_name not in", values, "nodeName");
            return (Criteria) this;
        }

        public Criteria andNodeNameBetween(String value1, String value2) {
            addCriterion("node_name between", value1, value2, "nodeName");
            return (Criteria) this;
        }

        public Criteria andNodeNameNotBetween(String value1, String value2) {
            addCriterion("node_name not between", value1, value2, "nodeName");
            return (Criteria) this;
        }

        public Criteria andNodeIpIsNull() {
            addCriterion("node_ip is null");
            return (Criteria) this;
        }

        public Criteria andNodeIpIsNotNull() {
            addCriterion("node_ip is not null");
            return (Criteria) this;
        }

        public Criteria andNodeIpEqualTo(String value) {
            addCriterion("node_ip =", value, "nodeIp");
            return (Criteria) this;
        }

        public Criteria andNodeIpNotEqualTo(String value) {
            addCriterion("node_ip <>", value, "nodeIp");
            return (Criteria) this;
        }

        public Criteria andNodeIpGreaterThan(String value) {
            addCriterion("node_ip >", value, "nodeIp");
            return (Criteria) this;
        }

        public Criteria andNodeIpGreaterThanOrEqualTo(String value) {
            addCriterion("node_ip >=", value, "nodeIp");
            return (Criteria) this;
        }

        public Criteria andNodeIpLessThan(String value) {
            addCriterion("node_ip <", value, "nodeIp");
            return (Criteria) this;
        }

        public Criteria andNodeIpLessThanOrEqualTo(String value) {
            addCriterion("node_ip <=", value, "nodeIp");
            return (Criteria) this;
        }

        public Criteria andNodeIpLike(String value) {
            addCriterion("node_ip like", value, "nodeIp");
            return (Criteria) this;
        }

        public Criteria andNodeIpNotLike(String value) {
            addCriterion("node_ip not like", value, "nodeIp");
            return (Criteria) this;
        }

        public Criteria andNodeIpIn(List<String> values) {
            addCriterion("node_ip in", values, "nodeIp");
            return (Criteria) this;
        }

        public Criteria andNodeIpNotIn(List<String> values) {
            addCriterion("node_ip not in", values, "nodeIp");
            return (Criteria) this;
        }

        public Criteria andNodeIpBetween(String value1, String value2) {
            addCriterion("node_ip between", value1, value2, "nodeIp");
            return (Criteria) this;
        }

        public Criteria andNodeIpNotBetween(String value1, String value2) {
            addCriterion("node_ip not between", value1, value2, "nodeIp");
            return (Criteria) this;
        }

        public Criteria andGuidLikeInsensitive(String value) {
            addCriterion("upper(guid) like", value.toUpperCase(), "guid");
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

        public Criteria andFilePathLikeInsensitive(String value) {
            addCriterion("upper(file_path) like", value.toUpperCase(), "filePath");
            return (Criteria) this;
        }

        public Criteria andFileNameLikeInsensitive(String value) {
            addCriterion("upper(file_name) like", value.toUpperCase(), "fileName");
            return (Criteria) this;
        }

        public Criteria andReceiverLikeInsensitive(String value) {
            addCriterion("upper(receiver) like", value.toUpperCase(), "receiver");
            return (Criteria) this;
        }

        public Criteria andTranuniqueLikeInsensitive(String value) {
            addCriterion("upper(tranunique) like", value.toUpperCase(), "tranunique");
            return (Criteria) this;
        }

        public Criteria andNodeNameLikeInsensitive(String value) {
            addCriterion("upper(node_name) like", value.toUpperCase(), "nodeName");
            return (Criteria) this;
        }

        public Criteria andNodeIpLikeInsensitive(String value) {
            addCriterion("upper(node_ip) like", value.toUpperCase(), "nodeIp");
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