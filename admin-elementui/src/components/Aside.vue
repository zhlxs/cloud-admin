<template>
  <!-- default-active="1-4-1" -->
  <el-menu class="el-menu-vertical-demo"
           @open="handleOpen"
           @close="handleClose"
           :collapse="isCollapse"
           background-color="#545c64"
           text-color="#fff"
           active-text-color="#ffd04b">
    <h3 v-if="!isCollapse">通用后台管理系统</h3>
    <h3 v-if="isCollapse">系统</h3>
    <el-menu-item :index="item.path+''"
                  v-for="item in noChildren"
                  :key="item.path"
                  @click="handleRoute(item)">
      <i :class="'el-icon-'+item.icon"></i>
      <span slot="title">{{item.label}}</span>
    </el-menu-item>
    <!-- 含有子菜单 -->
    <el-submenu :index="item.path+''"
                v-for="item in hasChildren"
                :key="item.path">
      <template slot="title">
        <i :class="'el-icon-'+item.icon"></i>
        <span slot="title">{{item.label}}</span>
      </template>
      <el-menu-item-group>
        <el-menu-item :index="item.path+''"
                      v-for="(item,index) in item.children"
                      :key="index"
                      @click="handleRoute(item)">
          <i :class="'el-icon-'+item.icon"></i>
          <span slot="title">{{item.label}}</span>
        </el-menu-item>
      </el-menu-item-group>
    </el-submenu>
  </el-menu>
</template>

<script>
export default {
  data () {
    return {
      // isCollapse: false, // 是否折叠菜单
      menu: [
        {
          path: '/',
          name: 'home',
          label: '首页',
          icon: 's-home',
          url: 'home'
        },
        {
          path: '/user',
          name: 'user',
          label: '用户管理',
          icon: 'user',
          url: 'user'
        },
        {
          path: '/auth',
          name: 'auth',
          label: '权限管理',
          icon: 'setting',
          url: 'auth'
        },
        {
          label: '其他',
          icon: 'menu',
          children: [
            {
              path: '/page1',
              name: 'page1',
              label: '页面一',
              icon: 'setting',
              url: 'page1'
            },
            {
              path: '/page2',
              name: 'page2',
              label: '页面二',
              icon: 'setting',
              url: 'page2'
            }
          ]
        }]
    }
  },
  methods: {
    handleOpen (key, keyPath) {
      console.log(key, keyPath)
    },
    handleClose (key, keyPath) {
      console.log(key, keyPath)
    },
    handleRoute (item) {
      // 路由跳转
      this.$router.push({ name: item.name })
      this.$store.commit('selectMenu', item)
    }
  },
  computed: {
    noChildren () {
      return this.menu.filter(item => !item.children)
    },
    hasChildren () {
      return this.menu.filter(item => item.children)
    },
    isCollapse () {
      return this.$store.state.tab.isCollapse
    }
  }
}
</script>

<style lang="scss" scoped>
.el-menu {
  height: 100%;
  border: none;
  h3 {
    color: #ffffff;
    text-align: center;
    line-height: 48px;
  }
}
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px;
  min-height: 400px;
}
</style>
