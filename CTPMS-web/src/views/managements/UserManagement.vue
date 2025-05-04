
<template>
    <v-row>
        <v-col cols="12" md="12">
            <v-card>
                <!-- 搜索框 -->
                <v-card-title>
                    <v-row align="center" justify="start" style="width: 100%;" class="mt-2 mb-2 ml-2">
                        <span style="font-size: 15px;">用户ID：</span>
                        <v-text-field
                            v-model="searchName"
                            label="请输入用户姓名"
                            single-line
                            hide-details
                            variant="outlined"
                            style="margin-right: 10px;"
                        ></v-text-field>
                        <span style="font-size: 15px;">用户性别：</span>
                        <v-select
                            v-model="searchGender"
                            :items="genderOptions"
                            label="请选择用户性别"
                            single-line
                            hide-details
                            variant="outlined"
                            style="margin-right: 10px;"
                        ></v-select>
                        <v-btn color="primary" @click="searchUsers">搜索</v-btn>
                        <v-btn class="ml-2" color="primary" @click="resetUsers">重置</v-btn>
                    </v-row>
                </v-card-title>
            </v-card>
        </v-col>
        <v-col cols="12" md="12">
            <v-card>
                <!-- 自定义标题 -->
                <v-card-title>
                    <v-row align="center" justify="space-between" style="width: 100%;">
                        <div style="margin-top: 20px; margin-bottom: 20px; margin-left: 10px;">用户管理</div>
                        <v-btn style="margin-top: 5px; margin-bottom: 5px;" color="primary" @click="openAddUserDialog">添加用户</v-btn>
                    </v-row>
                </v-card-title>

                <v-data-table-server :items-length="totalUsers"
                                     v-model:items-per-page="itemsPerPage" @update:options="loadItems" class="table-responsive">
                    <thead>
                    <tr style="white-space: nowrap;">
                        <th class="text-uppercase">ID</th>
                        <th class="text-center">姓名</th>
                        <th class="text-center">昵称</th>
                        <th class="text-center">性别</th>
                        <th class="text-center">年龄</th>
                        <th class="text-center">生日</th>
                        <th class="text-center">手机号</th>
                        <th class="text-center">住址</th>
                        <th class="text-center">邮箱</th>

                        <th class="text-center">账户余额</th>
                        <th class="text-center">级别</th>
                        <th class="text-center">状态</th>
                        <th class="text-center">操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr v-for="item in users" :key="item.id" style="white-space: nowrap;">
                        <td>{{ item.id }}</td>
                        <td class="text-center">{{ item.name }}</td>
                        <td class="text-center">{{ item.nickName }}</td>
                        <td class="text-center">{{ item.sex }}</td>
                        <td class="text-center">{{ item.age }}</td>
                        <td class="text-center">{{ item.birthday }}</td>
                        <td class="text-center">{{ item.phone }}</td>
                        <td class="text-center">{{ item.address }}</td>
                        <td class="text-center">{{ item.email }}</td>

                        <td class="text-center">{{ item.account }}</td>
                        <td class="text-center">{{ item.level }}</td>
                        <td class="text-center">
                            {{ item.status === 1 ? "正常" : "停用" }}
                        </td>
                        <td class="text-center" style="min-width: 200px;">
                            <v-btn color="primary" @click="openModifyUserDialog(item)">修改</v-btn>
                            <v-btn color="error" @click="confirmDeleteUser(item)" class="ml-5">删除</v-btn>
                        </td>
                    </tr>
                    </tbody>
                </v-data-table-server>
            </v-card>
        </v-col>

        <!-- 添加用户弹窗 -->
        <v-dialog v-model="isAddUserDialogOpen" max-width="600px">
            <v-card title="添加用户">
                <v-card-text>
                    <v-form>
                        <v-row>
                            <v-col cols="6">
                                <v-text-field variant="outlined" v-model="newUser.name" label="姓名"></v-text-field>
                            </v-col>
                            <v-col cols="6">
                                <v-text-field variant="outlined" v-model="newUser.nickName" label="昵称"></v-text-field>
                            </v-col>
                        </v-row>
                        <v-row>
                            <v-col cols="6">
                                <v-select variant="outlined" v-model="newUser.sex" :items="genderOptions" label="性别"></v-select>
                            </v-col>
                            <v-col cols="6">
                                <v-text-field variant="outlined" v-model="newUser.age" label="年龄" type="number"></v-text-field>
                            </v-col>
                        </v-row>
                        <v-row>
                            <v-col cols="6">
                                <v-text-field variant="outlined" v-model="newUser.birthday" label="生日" type="datetime-local"></v-text-field>
                            </v-col>
                            <v-col cols="6">
                                <v-text-field variant="outlined" v-model="newUser.phone" label="手机号"></v-text-field>
                            </v-col>
                        </v-row>
                        <v-row>
                            <v-col cols="6">
                                <v-text-field variant="outlined" v-model="newUser.address" label="住址"></v-text-field>
                            </v-col>
                            <v-col cols="6">
                                <v-text-field variant="outlined" v-model="newUser.email" label="邮箱"></v-text-field>
                            </v-col>
                        </v-row>

                    </v-form>
                </v-card-text>
                <v-card-actions>
                    <v-btn @click="isAddUserDialogOpen = false">取消</v-btn>
                    <v-btn color="primary" @click="addUser">新增</v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>

        <!-- 修改用户弹窗 -->
        <v-dialog v-model="isModifyUserDialogOpen" max-width="600px">
            <v-card title="修改用户">

                <v-card-text>
                    <v-form>
                        <v-row>
                            <v-col cols="6">
                                <v-text-field variant="outlined" v-model="selectedUser.name" label="姓名"></v-text-field>
                            </v-col>
                            <v-col cols="6">
                                <v-text-field variant="outlined" v-model="selectedUser.nickName" label="昵称"></v-text-field>
                            </v-col>
                        </v-row>
                        <v-row>
                            <v-col cols="6">
                                <v-select variant="outlined" v-model="selectedUser.sex" :items="genderOptions" label="性别"></v-select>
                            </v-col>
                            <v-col cols="6">
                                <v-text-field variant="outlined" v-model="selectedUser.age" label="年龄" type="number"></v-text-field>
                            </v-col>
                        </v-row>
                        <v-row>
                            <v-col cols="6">
                                <v-text-field variant="outlined" v-model="selectedUser.birthday" label="生日" type="datetime-local"></v-text-field>
                            </v-col>
                            <v-col cols="6">
                                <v-text-field variant="outlined" v-model="selectedUser.phone" label="手机号"></v-text-field>
                            </v-col>
                        </v-row>
                        <v-row>
                            <v-col cols="6">
                                <v-text-field variant="outlined" v-model="selectedUser.address" label="住址"></v-text-field>
                            </v-col>
                            <v-col cols="6">
                                <v-text-field variant="outlined" v-model="selectedUser.email" label="邮箱"></v-text-field>
                            </v-col>
                        </v-row>
                        <v-row>
                            <v-col cols="6">
                                <v-text-field variant="outlined" v-model="selectedUser.cardId" label="银行卡号"></v-text-field>
                            </v-col>
                            <v-col cols="6">
                                <v-text-field variant="outlined" v-model="selectedUser.account" label="账户余额" type="number"></v-text-field>
                            </v-col>
                        </v-row>
                    </v-form>
                </v-card-text>
                <v-card-actions>
                    <v-btn @click="isModifyUserDialogOpen = false">取消</v-btn>
                    <v-btn color="primary" @click="modifyUser">修改</v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>

        <!-- 确认删除用户弹窗 -->
        <v-dialog v-model="isDeleteUserDialogOpen" max-width="500px">
            <v-card>
                <v-card-title>确认删除</v-card-title>
                <v-card-text>你确定要删除用户 {{ selectedUser.name }} 吗？</v-card-text>
                <v-card-actions>
                    <v-btn @click="isDeleteUserDialogOpen = false">取消</v-btn>
                    <v-btn color="error" @click="deleteUser">删除</v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>
    </v-row>
</template>
<script setup lang="ts">


</script>