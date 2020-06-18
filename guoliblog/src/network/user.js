import {
  getRequestUser,
  postRequestUser,
  deleteRequestUser,
  putRequestUser
} from './axios'

//分页获取博客
export function getReleaseBlogList(queryCriteria) {
  return postRequestUser(`/bloginfo/pageBlog/releaser/${queryCriteria.current}/${queryCriteria.size}`, queryCriteria.info)
}

//获取标签列表
export function getLabes() {
  return getRequestUser(`/classificati/open`)
}
//根据id获取博客
export function getReleaseBlog(id) {
  return getRequestUser(`/bloginfo/getReleaseBlog/${id}`)
}

//用户登录
export function login(loginInfo) {
  return postRequestUser(`/ucenter/openup/login`, loginInfo)
}
//修改个人信息
export function updateMyInfo(updateInfo) {
  return putRequestUser(`/ucenter/openup/updateinfo`, updateInfo)
}
//注册用户
export function register(registerInfo) {
  return postRequestUser(`/ucenter/openup/register`, registerInfo)
}
//修改密码
export function changePasswordMethod(passwordInfo) {
  return putRequestUser(`/ucenter/openup/updatepassword`, passwordInfo)
}

//获取验证码
export function getcode(phone) {
  return getRequestUser(`/edumsm/msm/send/${phone}`)
}
//根据blogid获取评论信息
export function getcomment(blogid) {
  return getRequestUser(`/comment/openup/${blogid}`)
}
//根据blogid 和 puid 获取回复信息
export function getreply(blogid, pid, uid) {
  return getRequestUser(`/comment/openup/${blogid}/${pid}/${uid}`)
}
//回复-添加评论
export function addNewContent(commentInfo) {
  return postRequestUser(`/comment/openup`, commentInfo)
}
//撤回评论
export function withdrawContent(id) {
  return deleteRequestUser(`/comment/openup/${id}`)
}
//获取总数据
export function getTotalNumber() {
  return getRequestUser(`/totalnumber`)
}