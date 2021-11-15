<template>
  <el-row class="home"
          :gutter="20">
    <el-col :span="8"
            style="margin-top:20px;">
      <!-- 个人卡片 -->
      <el-card shadow="hover">
        <div class="user">
          <img :src="userImg" />
          <div class="userInfo">
            <p class="name">Admin</p>
            <p class="access">超级管理员</p>
          </div>
          <div class="login-info">
            <p>
              上次登录时间：<span>2021-11-01</span>
            </p>
            <p>
              上次登录地点：<span>江西南昌</span>
            </p>
          </div>
        </div>
      </el-card>
    </el-col>
    <el-col :span="16">

    </el-col>
  </el-row>
</template>

<script>

export default {
  data () {
    return {
      userImg: require('../../assets/images/user.png')
    }
  }
}

</script>

<style lang="scss" scoped>
@import "~@/assets/scss/home";
</style>
