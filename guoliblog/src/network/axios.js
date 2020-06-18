import axios from 'axios' //导入axios

import NProgress from 'nprogress'
import 'nprogress/nprogress.css'


axios.defaults.baseURL = 'http://localhost:8881' // -> 设置默认请求地址
axios.defaults.timeout = 5000 // -> 设置请求超时时间

//封装管理员的API方法==========================================================
//封装get方法
export const getRequest = (url, params) => {
  return axios({
    url,
    params,
    method: 'get',
    withcredentials: true,
    headers: {
      'Authorization': sessionStorage.getItem('admintoken')
    }
  })
}


//封装post方法
export const postRequest = (url, data) => {
  return axios({
    url: url,
    data: data,
    method: 'post',
    withcredentials: true,
    headers: {
      'Authorization': sessionStorage.getItem('admintoken')
    }
  })
}

//封装put方法
export const putRequest = (url, params) => {
  return axios({
    url: url,
    data: params,
    method: 'put',
    withCredentials: true,
    headers: {
      'Authorization': sessionStorage.getItem('admintoken')
    }
  })
}
//封装delete方法
export const deleteRequest = (url, params) => {
  return axios({
    url: url,
    data: params,
    method: 'delete',
    withCredentials: true,
    headers: {
      'Authorization': sessionStorage.getItem('admintoken')
    }
  })
}


//封装用户的API方法==========================================================
export const getRequestUser = (url, params) => {
  return axios({
    url,
    params,
    method: 'get',
    withcredentials: true
  })
}


//封装post方法
export const postRequestUser = (url, data) => {
  return axios({
    url: url,
    data: data,
    method: 'post',
    withcredentials: true,
    headers: {
      'token': sessionStorage.getItem('token')
    }
  })
}

//封装put方法
export const putRequestUser = (url, params) => {
  return axios({
    url: url,
    data: params,
    method: 'put',
    withCredentials: true,
    headers: {
      'token': sessionStorage.getItem('token')
    }
  })
}
//封装delete方法
export const deleteRequestUser = (url, params) => {
  return axios({
    url: url,
    data: params,
    method: 'delete',
    withCredentials: true,
    headers: {
      'token': sessionStorage.getItem('token')
    }
  })
}


//请求拦截
axios.interceptors.request.use(config => {
  NProgress.start();
  return config
}, err => {})

//响应拦截
axios.interceptors.response.use(res => {
  //操作后返回出去
  NProgress.done();
  return res.data
}, err => {
  NProgress.done();
  const obj = {}
  obj.status = 201
  obj.message = '你暂时无此项权限'
  return obj
})