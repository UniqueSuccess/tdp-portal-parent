package cn.goldencis.tdp.common.service;

import cn.goldencis.tdp.common.entity.BaseEntity;

import java.util.List;

/**
 *
 * @param <T>
 * @param <C><br/>
 * @author liuzhh.
 */
public interface BaseService<T extends BaseEntity, C> {

    public boolean isExists(String id);

    public boolean isExists(Integer id);

    /**
     * 根据实体对象新增记录.
     *
     * @param entity
     *            .
     * @return id .
     */
    T create(T entity);

    /**
     *
     * @param entity
     * @return
     */
    T createSelective(T entity);

    /**
     * 更新实体对应的记录.
     *
     * @param entity
     *            .
     */
    void update(T entity);

    void updateByExample(T entity, C example);

    void updateByExampleSelective(T entity, C example);

    /**
     * 批量更新对象.
     *
     * @param entity
     *            .
     */
    void update(List<T> list);

    void create(List<T> list);

    /**
     * 根据ID删除记录.
     *
     * @param id
     *            .
     */
    void deleteByPrimaryKey(String primaryKey);
    void deleteByPrimaryKey(Integer primaryKey);

    /**
     * 根据ID查找记录.
     *
     * @param id
     *            .
     * @return entity .
     */
    T getByPrimaryKey(String primaryKey);
    T getByPrimaryKey(Integer primaryKey);

    /**
     *
     * @param offset
     * @param limit
     * @param example
     * @return
     */
    public List<T> listPage(int offset, int limit, C example);

    /**
     *
     * @param example
     * @return
     */
    public List<T> listBy(C example);

    /**
     *
     * @param example
     * @return
     */
    T getBy(C example);

}
