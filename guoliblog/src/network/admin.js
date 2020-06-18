import {
  getRequest,
  postRequest,
  deleteRequest,
  putRequest
} from './axios'
//用户登录
export function adminlogin(userInfo) {
  return postRequest(`/login`, userInfo)
}
//获取用户列表
export function getUserlList() {
  return getRequest(`/user/search`)
}
//根据id删除用户
export function deleteUser(userId) {
  return deleteRequest(`/user?idList=${userId}`)
}
//添加用户
export function addUser(userInfo) {
  return postRequest(`/user/register`, userInfo)
}
//修改用户
export function updateUserInfo(userInfo) {
  return putRequest(`/user`, userInfo)
}

//获取角色列表
export function getRoleList(current, size) {
  return getRequest(`/sysRoleTable/${current}/${size}`)
}
//添加新角色
export function addRole(roleinfo) {
  return postRequest(`/sysRoleTable`, roleinfo)
}
//修改角色信息
export function updateRole(roleinfo) {
  return putRequest(`/sysRoleTable`, roleinfo)
}
//删除角色
export function deleteRole(roleId) {
  return deleteRequest(`/sysRoleTable?idList=${roleId}`)
}
//根据id查询授权信息
export function getRoleInfoList(url, roleId) {
  return getRequest(`/sysRoleTable/${url}/${roleId}`)
}
//授权
export function setRoleInfo(url, roleId, RoleInfo) {
  return postRequest(`/sysRoleTable/${url}/set?roleId=${roleId}`, RoleInfo)
}

//获取菜单数据
export function getMenuList() {
  return getRequest(`/sysFrontendMenuTable`)
}
//添加新菜单
export function addMenu(menuInfo) {
  return postRequest(`/sysFrontendMenuTable`, menuInfo)
}
//跟新菜单信息
export function updateMenu(menuInfo) {
  return putRequest(`/sysFrontendMenuTable`, menuInfo)
}
//删除菜单
export function deleteMenu(id) {
  return deleteRequest(`/sysFrontendMenuTable?idList=${id}`)
}

//获取api列表
export function getApiList() {
  return getRequest(`/sysBackendApiTable`)
}
//添加API
export function addApi(apiInfo) {
  return postRequest(`/sysBackendApiTable`, apiInfo)
}
//更新API
export function updateApi(apiInfo) {
  return putRequest(`/sysBackendApiTable`, apiInfo)
}
//删除API
export function deleteApi(id) {
  return deleteRequest(`/sysBackendApiTable?idList=${id}`)
}


//获取走马灯列表
export function getCarouselImgList() {
  return getRequest(`/carouselImg`)
}
//添加走马灯
export function addCarouselImg(Obj) {
  return postRequest(`/carouselImg`, Obj)
}
//根据id删除走马灯
export function deleteCarouselImg(id) {
  return deleteRequest(`/carouselImg/${id}`)
}


//==================================
//图片上传
export function imgadd(formdata) {
  return postRequest(`/blogoss/fileoss`, formdata)
}


//-----------------------------------

//获取博客总数据
export function getBlogData() {
  return getRequest(`/bloginfo`)
}

//分页获取博客
export function getBlogList(queryCriteria) {
  return postRequest(`/bloginfo/pageBlogCondition/${queryCriteria.current}/${queryCriteria.size}`, queryCriteria.condition)
}
//根据id获取博客
export function getByIdBlog(id) {
  return getRequest(`/bloginfo/getBlog/${id}`)
}
//添加博客
export function addBlog(bloginfo) {
  return postRequest(`/bloginfo/addBlog`, bloginfo)
}
//修改博客
export function updateBlog(bloginfo) {
  return putRequest(`/bloginfo/updateBlog`, bloginfo)
}
//修改博客状态
export function updateBlogState(bloginfo) {
  return putRequest(`/bloginfo/updateBlogState`, bloginfo)
}
//根据id删除博客
export function deleteBlog(id) {
  return deleteRequest(`/bloginfo/delete/${id}`)
}
//根据lid获取博客
export function getLabeBlogList(lid) {
  return getRequest(`/bloginfo/getLabedBlog/${lid}`)
}



//获取标签列表
export function getLabeList() {
  return getRequest(`/classificati`)
}
//添加标签
export function addLabe(labeInfo) {
  return postRequest(`/classificati`, labeInfo)
}
//修改标签
export function updateLabe(labeInfo) {
  return putRequest(`/classificati`, labeInfo)
}
//根据id删除标签
export function deleteLabe(id) {
  return deleteRequest(`/classificati/${id}`)
}


// ===============================================
//分页获取评论用户列表
export function getUcenterList(selectInfo) {
  return postRequest(`/ucenter/page/${selectInfo.current}/${selectInfo.size}`, selectInfo.likes)
}
//根据uid获取用户信息
export function getByUid(uid) {
  return getRequest(`/ucenter/byuid/${uid}`)
}
//操作用户
export function updateUcenterInfo(updateInfo) {
  return postRequest(`/ucenter`, updateInfo)
}
//根据id删除用户
export function deleteByuid(uid) {
  return deleteRequest(`/ucenter/${uid}`)
}
//分页获取评论列表
export function getCommentList(selectInfo) {
  return postRequest(`/comment/page/${selectInfo.current}/${selectInfo.size}/${selectInfo.uid}`)
}
//批量删除
export function batchdelete(deleteids) {
  return deleteRequest(`/comment/batchdelete`, deleteids)
}