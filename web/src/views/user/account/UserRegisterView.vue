<template>
    <ContentField>
        <div class="row justify-content">
            <div class="col-8">
                <div id="carouselExampleCaptions" class="carousel slide" data-bs-ride="carousel">
                    <div class="carousel-indicators">
                        <button type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide-to="0" class="active" aria-current="true" aria-label="Slide 1"></button>
                        <button type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide-to="1" aria-label="Slide 2"></button>
                        <button type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide-to="2" aria-label="Slide 3"></button>
                    </div>
                    <div class="carousel-inner">
                        <div class="carousel-item active">
                            <img src="../../../assets/set/img_7.png" class="d-block w-100" alt="...">
                            <div class="carousel-caption d-none d-md-block">
                                <h5>First slide label</h5>
                                <p>Some representative placeholder content for the first slide.</p>
                            </div>
                        </div>
                        <div class="carousel-item">
                            <img src="../../../assets/set/img_12.png" class="d-block w-100" alt="...">
                            <div class="carousel-caption d-none d-md-block">
                                <h5>Second slide label</h5>
                                <p>Some representative placeholder content for the second slide.</p>
                            </div>
                        </div>
                        <div class="carousel-item">
                            <img src="../../../assets/set/img_15.png" class="d-block w-100" alt="...">
                            <div class="carousel-caption d-none d-md-block">
                                <h5>Third slide label</h5>
                                <p>Some representative placeholder content for the third slide.</p>
                            </div>
                        </div>
                    </div>
                    <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide="prev">
                        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                        <span class="visually-hidden">Previous</span>
                    </button>
                    <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide="next">
                        <span class="carousel-control-next-icon" aria-hidden="true"></span>
                        <span class="visually-hidden">Next</span>
                    </button>
                </div>
            </div>
            <div class="col-3"  style="margin: 0vh 5vh 5vh;">
                <p class="fw-light" style="font-size: 50px; margin-bottom: 3vh">来敲你码</p>
                <form @submit.prevent="register">
                    <div class="mb-3">
                        <label for="username" class="form-label">用户名</label>
                        <input v-model="username" type="text" class="form-control" id="username" placeholder="请输入用户名">
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">密码&确认密码</label>
                        <input v-model="password" type="password" class="form-control" id="password"
                               placeholder="请输入密码">
                    </div>
                    <div class="mb-3">
<!--                        <label for="confirmedPassword" class="form-label">确认密码</label>-->
                        <input v-model="confirmedPassword" type="password" class="form-control" id="confirmedPassword"
                               placeholder="请再次输入密码">
                    </div>
                    <div class="error_message" style="margin: 1vh; min-height: 4vh">{{error_message}}</div>
                    <div class="d-grid gap-2">
                        <button type="submit" class="btn btn-primary">注册</button>
                    </div>
                </form>
            </div>
        </div>
    </ContentField>
 </template>
   
 <script>
import ContentField from '@/components/ContentField.vue'
import {ref} from 'vue'
import router from '@/router/index'
import $ from 'jquery'
export default {
    components: {ContentField},
    setup() {
        let username = ref('');
        let password = ref('');
        let confirmedPassword = ref('');
        let error_message = ref('');

        const register = () => {
            $.ajax({
                url: "http://127.0.0.1:3000/user/account/register/",
                type: 'post',
                data: {
                    username: username.value,
                    password: password.value,
                    confirmedPassword: confirmedPassword.value,
                },
                success(resp) {
                    if (resp.error_message === "success") {
                        router.push({name: "user_account_login"})
                    } else {
                        error_message.value = resp.error_message;
                    }
                },
                error(resp) {
                    console.log(resp);
                }
            });
        }
        return {
            username,
            password,
            confirmedPassword,
            error_message,
            register,

        }
    }
}
 </script>
 
 <style scoped>
 div.error_message{
   color: red
 }
 </style>