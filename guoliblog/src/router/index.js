import Vue from 'vue'
import VueRouter from 'vue-router'

const Login = () => import('views/admin/login/Login')
const Home = () => import('views/admin/home/Home')

const BlogInterface = () => import('views/bloginterface/BlogInterface')
const BlogInfo = () => import('views/bloginterface/bloginfo/BlogInfo')

const Control = () => import('views/admin/home/childComps/Control')
const BlogAdmin = () => import('views/admin/home/childComps/BlogAdmin')
const LabelControl = () => import('views/admin/home/childComps/LabelControl')
const CommentUcenbter = () => import('views/admin/home/childComps/CommentUcenbter')
const CommentContent = () => import('views/admin/home/childComps/CommentContent')
const EditArticle = () => import('views/admin/home/childComps/EditArticle')

const User = () => import('views/admin/home/childComps/User')
const Role = () => import('views/admin/home/childComps/Role')
const Menu = () => import('views/admin/home/childComps/Menu')
const Api = () => import('views/admin/home/childComps/Api')



Vue.use(VueRouter)

const routes = [{
  path: '/',
  redirect: '/bloginterface'
}, {
  path: '/login',
  component: Login
}, {
  path: '/home',
  component: Home,
  children: [{
    path: '',
    redirect: '/home/control'
  }, {
    path: '/home/control',
    component: Control
  }, {
    path: '/home/blogAdmin',
    component: BlogAdmin
  }, {
    path: '/home/labelControl',
    component: LabelControl
  }, {
    path: '/home/commentUcenbter',
    component: CommentUcenbter
  }, {
    path: '/home/commentContent',
    component: CommentContent
  }, {
    path: '/home/editArticle',
    component: EditArticle
  }, {
    path: '/home/user',
    component: User
  }, {
    path: '/home/role',
    component: Role
  }, {
    path: '/home/menu',
    component: Menu
  }, {
    path: '/home/api',
    component: Api
  }]
}, {
  path: '/bloginterface',
  component: BlogInterface
}, {
  path: '/bloginfo',
  component: BlogInfo
}]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to, from, next) => {
  if (to.path === '/login' || to.path === '/bloginterface' || to.path === '/bloginfo') return next()
  const tokenStr = window.sessionStorage.getItem('admintoken')
  if (!tokenStr) return next('/login')
  next()
})
export default router