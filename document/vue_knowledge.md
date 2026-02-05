```
git remote add origin https://github.com/MadGodBob/vue-demo.git
git branch -M main
git push -u origin main
```

下载nodejs	http://nodejs.cn/download/

## 安装Vue脚手架

```
npm install -g @vue/cli
```

进入工作区创建Vue项目(名称内不能有大写字母)，之后上下方向键选择Vue3按回车。完成后测试

```
vue create vuedemo
cd vuedemo
npm run serve
```

## 导入Element-ui

进入工作空间

```
npm install element-plus --save
```

将main.js修改为

```
import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'

const app = createApp(App)

app.use(ElementPlus)
app.mount('#app')
```

修改App.vue进行测试

```
<template>
  <div id="app">
    <el-button>Test</el-button>
  </div>
</template>

<script>

export default {
  name: 'App',
  components: {

  }
}
</script>
```

## 搭建页面布局

Element-ui官方组件库

```
https://element-plus.org/zh-CN/component/overview
```

