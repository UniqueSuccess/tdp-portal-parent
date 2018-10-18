/**
 *
 */
package cn.goldencis.tdp.core.dao;

import java.util.List;

import cn.goldencis.tdp.core.entity.UserDO;
import cn.goldencis.tdp.core.entity.UserDepartmentDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import cn.goldencis.tdp.common.annotation.Mybatis;
import cn.goldencis.tdp.core.entity.DepartmentDO;

@Mybatis
public interface CustomizedMapper {

    void addUserDeparts(List<UserDepartmentDO> userDepartments);

    @Select({
            "<script>",
            "SELECT d.id, d.name, p.name as parentName,d.ip_part as ipPart,d.parent_id as parentId, d.owner,d.department_tel, d.tree_path as treePath as departmentTel FROM t_department d left join t_department p on d.parent_id=p.id ",
            "WHERE ( d.status=1 ", "<if test='treePath != null'>", "AND d.tree_path like #{treePath} ", "</if>", ")",
            "<if test='pId != null and pId != 1'>", " or (d.id=#{pId}) ", "</if>", "<if test='ordercase != null '>",
            " order by ${ordercase} ", "</if>", "<if test='startNum != null and pageSize != null'>",
            "limit #{startNum},#{pageSize} ", "</if>", "</script>"})
    List<DepartmentDO> getDeptarMentList(@Param("startNum") Integer startNum, @Param("pageSize") Integer pageSize, @Param("pId") Integer pId, @Param("treePath") String treePath, @Param("ordercase") String ordercase);

    Long countUserList(UserDO user);

    List<UserDO> getUserList(UserDO user);

    @Select({
            "<script>",
            "SELECT d.id, d.name, d.parent_id, d.department_remark, d.owner, d.department_tel, d.ip_part, d.tree_path ",
            "FROM t_user_department ud left join t_department d on ud.department_id=d.id ",
            "WHERE  1=1 AND ud.user_id = #{userGuid} ", "</script>"})
    List<DepartmentDO> getUserDepartmentByUser(@Param("userGuid") String userGuid);

    /**
     * 与getUserRoleDepartmentByUser以后所不同,不用 like 用INSTR, 只需要传入一级菜单id,减少遍历次数
     *
     * @param ids
     * @return List<DepartmentDO>
     * @author mll
     */
    @Select({
            "<script>",
            "SELECT d.id, d.name, d.parent_id as parentId,d.ip_part as ipPart ,d.tree_path AS treePath, d.owner,d.department_tel as departmentTel  ",
            "FROM  t_department d  WHERE ",
            "<foreach item='did' index='index' collection='ids' open='(' separator='OR' close=')'>",
            " INSTR(d.tree_path,',${did},') OR d.id=#{did}", "</foreach>", "</script>"})
    List<DepartmentDO> getUserRoleDepartmentByParentIdOptimised(@Param("ids") List<String> ids);

    String getUserIdListByApproveModelId(@Param(value = "approveModelId") Integer approveModelId);

}
