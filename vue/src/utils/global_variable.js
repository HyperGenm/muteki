/**
 * 全局常量
 * @type {{}}
 */
const GLOBAL = {
    //请求前缀
    baseUrl: process.env.VUE_APP_URL,
    //请求头token名称
    token: 'token',
    //存储storage前缀,用于区分相同域名下的其他项目
    storagePrefix: 'weiziplus'
};

/**
 * 请求url
 * @type {{}}
 */
const URL = {
    /*登录 */
    login: '/login',
    /*登录验证码*/
    loginValidateCode: '/getValidateCode',
    /*退出登录*/
    logout: '/logout',
    /*************系统管理*****************/
    system: {
        /*********************用户管理*****************/
        sysUser: {
            /*获取分页数据*/
            getPageList: '/sysUser/getPageList',
            /*修改头像*/
            updateIcon: '/sysUser/updateIcon',
            /*修改密码*/
            updatePwd: '/sysUser/updatePwd',
            /*修改状态*/
            updateStatus: '/sysUser/updateStatus',
            /*修改手机号码*/
            updatePhone: '/sysUser/updatePhone',
            /*修改角色*/
            updateRole: '/sysUser/updateRole',
            /*重置密码*/
            resetPwd: '/sysUser/resetPwd',
            /*新增*/
            add: '/sysUser/add',
            /*删除*/
            delete: '/sysUser/delete',
        },
        /*********************角色管理*****************/
        sysRole: {
            /*获取分页数据*/
            getPageList: '/sysRole/getPageList',
            /*获取列表*/
            getList: '/sysRole/getList',
            /*获取角色拥有的功能列表*/
            getFunctionList: '/sysRole/getFunctionList',
            /*修改角色功能*/
            updateRoleFunction: '/sysRole/updateRoleFunction',
            /*修改状态*/
            updateStatus: '/sysRole/updateStatus',
            /*新增*/
            add: '/sysRole/add',
            /*修改*/
            update: '/sysRole/update',
            /*删除*/
            delete: '/sysRole/delete',
        },
        /*********************功能管理*****************/
        sysFunction: {
            /*获取分页数据*/
            getPageList: '/sysFunction/getPageList',
            /*获取树形结构*/
            getTree: '/sysFunction/getTree',
            /*新增*/
            add: '/sysFunction/add',
            /*修改*/
            update: '/sysFunction/update',
            /*删除*/
            delete: '/sysFunction/delete',
            /*修改功能拥有的api*/
            updateContainApi: '/sysFunction/updateContainApi',
        },
        /*********************系统用户日志*****************/
        sysUserLog: {
            /*获取分页数据*/
            getPageList: '/sysUserLog/getPageList',
        },
        /*********************系统异常*****************/
        sysError: {
            /*获取分页数据*/
            getPageList: '/sysError/getPageList'
        },
        /*********************用户日志*****************/
        userLog: {
            /*获取分页数据*/
            getPageList: '/userLog/getPageList'
        },
        /*********************系统文件*****************/
        sysFile: {
            /*获取日志文件*/
            getLogFile: '/sysFile/getLogFile',
            /*下载日志文件*/
            downLogFile: '/sysFile/downLogFile'
        },
        /*********************系统用户登录日志*****************/
        sysUserLoginLog: {
            /*获取分页数据*/
            getPageList: '/sysUserLoginLog/getPageList'
        },
        /*********************用户登录日志*****************/
        userLoginLog: {
            /*获取分页数据*/
            getPageList: '/userLoginLog/getPageList'
        },
        /*********************web用户*****************/
        user: {
            /*获取分页数据*/
            getPageList: '/user/getPageList',
            /*禁用账户*/
            disableUser: '/user/disableUser',
            /*启用账户*/
            enableUser: '/user/enableUser'
        },
    },
    /*************字典管理*****************/
    dataDictionary: {},
    /********常用工具*****************/
    tools: {
        /*文件上传*/
        upload: '/tools/upload',
        /*文件上传*/
        downTemp: '/tools/downTemp'
    },
};

export default {
    GLOBAL, URL
}