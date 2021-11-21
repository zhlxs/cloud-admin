<template>
  <div class="login-background">
    <div id="login-container">
      <div style="text-align: center;height: 50px">
        后台管理系统
      </div>
      <el-form :model="ruleForm"
               status-icon
               :rules="rules"
               ref="ruleForm"
               label-width="100px"
               class="demo-ruleForm">
        <el-form-item label="账号"
                      prop="user">
          <el-input type="text"
                    v-model="ruleForm.user"
                    autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="密码"
                      prop="pass">
          <el-input type="password"
                    v-model="ruleForm.pass"
                    autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary"
                     @click="submitForm('ruleForm')">提交</el-button>
          <el-button @click="resetForm('ruleForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import { mapActions } from 'vuex'
import user from '../../store/modules/user'
// import axios from 'axios'
export default {
  name: 'login',
  data () {
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入账户'))
      } else {
        if (this.ruleForm.pass !== '') {
          this.$refs.ruleForm.validateField('pass')
        }
        callback()
      }
    }
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else {
        callback()
      }
    }
    return {
      ruleForm: {
        user: '',
        pass: ''
      },
      rules: {
        user: [
          { validator: validatePass, trigger: 'blur' }
        ],
        pass: [
          { validator: validatePass2, trigger: 'blur' }
        ]
      },
      isLogin: false
    }
  },
  methods: {
    ...mapActions('saveLoginInfo', ['saveLoginAction']),
    saveLoginAction (data) {
      console.log(user)
      user.dispatch('saveLoginAction', data)
    },
    submitForm (formName) {
      const that = this
      this.$refs[formName].validate((valid) => {
        if (valid) {
          // this.$message.success('登录成功!!!')
          // this.$router.push({ path: '/' })
          // 实现登录逻辑
          this.$http.post('/api/login', this.$qs.stringify({
            username: this.ruleForm.user,
            password: this.ruleForm.pass
          })).then(function (res) {
            // 登录成功，页面跳转，缓存用户数据
            // 跳转到首页
            if (res.data && res.data.data.jwtToken && res.data.data.jwtToken.token) {
              sessionStorage.setItem('token', res.data.data.jwtToken.token)
              that.$store
                .dispatch('saveLoginAction', res.data.data)
                .then(() => { })
                .catch(() => { })
              // console.log(res.data.data)
              // that.saveLoginAction(res.data.data)
              that.$router.push({ name: 'home' })
            }
            // localStorage.setItem('ms_username', this.loginForm.username)
            // this.$router.push('/home')
          }).catch(() => {
            // 登录失败
          })
        }
      })
    },
    resetForm (formName) {
      this.$refs[formName].resetFields()
    }
  }
}
</script>

<style scoped>
body {
  margin: 0;
}
#login-container {
  width: 400px;
  height: 290px;
  background: #e5e9f2;
  /* background: url("../../assets/images/background.jpg"); */
  position: absolute;
  left: 50%;
  top: 50%;
  margin-left: -220px;
  margin-top: -170px;
  border-radius: 5px;
  padding-top: 40px;
  padding-right: 40px;
}
.login-background {
  position: absolute;
  width: 100%;
  height: 100%;
  background: url("../../assets/images/back.jpg") no-repeat center center;
  background-size: cover;
}
</style>
