package com.boot.system.mapper;

import com.boot.system.domain.SysFiles;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 上传文件信息Mapper接口
 * 
 * @author boot
 * @date 2019-10-12
 */
@Component
@Mapper
public interface SysFilesMapper {
    /**
     * 查询上传文件信息
     *
     * @param id 上传文件信息ID
     * @return 上传文件信息
     */
    public SysFiles selectSysFilesById(String id);
    public List<SysFiles> selectSysFilesByIds(String[] ids);
    /**
     * 查询上传文件信息列表
     *
     * @param sysFiles 上传文件信息
     * @return 上传文件信息集合
     */
    public List<SysFiles> selectSysFilesList(SysFiles sysFiles);

    /**
     * 新增上传文件信息
     *
     * @param sysFiles 上传文件信息
     * @return 结果
     */
    public int insertSysFiles(SysFiles sysFiles);

    /**
     * 修改上传文件信息
     *
     * @param sysFiles 上传文件信息
     * @return 结果
     */
    public int updateSysFiles(SysFiles sysFiles);

    /**
     * 批量删除上传文件信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysFilesByIds(String[] ids);
}
