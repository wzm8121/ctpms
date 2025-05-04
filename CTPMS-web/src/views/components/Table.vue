<script setup lang="ts">
import { ref } from 'vue';
import UiParentCard from '@/components/shared/UiParentCard.vue';
const staff = ref([
  {
    stfNo: '001',
    stfName: '张三',
    stfSex: '男',
    stfTel: '13800138000',
    stfQq: '12345678',
    stfEmail: 'zhangsan@example.com',
    stfIdno: '110101199003071234',
    stfLocation: '北京市海淀区',
    dpNo: 'IT001',
    stfLevel: '高级',
    stfCardno: '622202010112345678',
    stfStatus: '1'
  },
  {
    stfNo: '002',
    stfName: '李四',
    stfSex: '男',
    stfTel: '13800138001',
    stfQq: '87654321',
    stfEmail: 'lisi@example.com',
    stfIdno: '110101198504091234',
    stfLocation: '上海市浦东新区',
    dpNo: 'HR001',
    stfLevel: '中级',
    stfCardno: '622202010112345679',
    stfStatus: '1'
  },
  {
    stfNo: '003',
    stfName: '王五',
    stfSex: '女',
    stfTel: '13800138002',
    stfQq: '23456789',
    stfEmail: 'wangwu@example.com',
    stfIdno: '110101199006071234',
    stfLocation: '广州市天河区',
    dpNo: 'FIN001',
    stfLevel: '初级',
    stfCardno: '622202010112345680',
    stfStatus: '1'
  },
  {
    stfNo: '004',
    stfName: '赵六',
    stfSex: '女',
    stfTel: '13800138003',
    stfQq: '34567890',
    stfEmail: 'zhaoliu@example.com',
    stfIdno: '110101199207081234',
    stfLocation: '深圳市南山区',
    dpNo: 'IT002',
    stfLevel: '高级',
    stfCardno: '622202010112345681',
    stfStatus: '0'
  },
  {
    stfNo: '005',
    stfName: '孙七',
    stfSex: '男',
    stfTel: '13800138004',
    stfQq: '45678901',
    stfEmail: 'sunqi@example.com',
    stfIdno: '110101198807091234',
    stfLocation: '成都市高新区',
    dpNo: 'IT001',
    stfLevel: '中级',
    stfCardno: '622202010112345682',
    stfStatus: '1'
  },
  {
    stfNo: '006',
    stfName: '周八',
    stfSex: '女',
    stfTel: '13800138005',
    stfQq: '56789012',
    stfEmail: 'zhouba@example.com',
    stfIdno: '110101199308101234',
    stfLocation: '武汉市武昌区',
    dpNo: 'HR002',
    stfLevel: '初级',
    stfCardno: '622202010112345683',
    stfStatus: '0'
  },
  {
    stfNo: '007',
    stfName: '吴九',
    stfSex: '男',
    stfTel: '13800138006',
    stfQq: '67890123',
    stfEmail: 'wujou@example.com',
    stfIdno: '110101199409111234',
    stfLocation: '南京市玄武区',
    dpNo: 'FIN002',
    stfLevel: '高级',
    stfCardno: '622202010112345684',
    stfStatus: '1'
  },
  {
    stfNo: '008',
    stfName: '郑十',
    stfSex: '女',
    stfTel: '13800138007',
    stfQq: '78901234',
    stfEmail: 'zhengshi@example.com',
    stfIdno: '110101199507121234',
    stfLocation: '杭州市西湖区',
    dpNo: 'IT002',
    stfLevel: '中级',
    stfCardno: '622202010112345685',
    stfStatus: '1'
  },
  {
    stfNo: '009',
    stfName: '王一',
    stfSex: '男',
    stfTel: '13800138008',
    stfQq: '89012345',
    stfEmail: 'wangyi@example.com',
    stfIdno: '110101199108131234',
    stfLocation: '长沙市雨花区',
    dpNo: 'HR002',
    stfLevel: '初级',
    stfCardno: '622202010112345686',
    stfStatus: '0'
  },
  {
    stfNo: '010',
    stfName: '李二',
    stfSex: '女',
    stfTel: '13800138009',
    stfQq: '90123456',
    stfEmail: 'lier@example.com',
    stfIdno: '110101199309141234',
    stfLocation: '重庆市渝中区',
    dpNo: 'FIN002',
    stfLevel: '高级',
    stfCardno: '622202010112345687',
    stfStatus: '1'
  },
  {
    stfNo: '011',
    stfName: '张三',
    stfSex: '男',
    stfTel: '13800138010',
    stfQq: '01234567',
    stfEmail: 'zhangsansan@example.com',
    stfIdno: '110101199410151234',
    stfLocation: '北京市朝阳区',
    dpNo: 'IT001',
    stfLevel: '中级',
    stfCardno: '622202010112345688',
    stfStatus: '1'
  },
  {
    stfNo: '012',
    stfName: '李四',
    stfSex: '女',
    stfTel: '13800138011',
    stfQq: '12345678',
    stfEmail: 'lisi@example.com',
    stfIdno: '110101199011161234',
    stfLocation: '上海市静安区',
    dpNo: 'HR001',
    stfLevel: '初级',
    stfCardno: '622202010112345689',
    stfStatus: '0'
  },
  {
    stfNo: '013',
    stfName: '王五',
    stfSex: '男',
    stfTel: '13800138012',
    stfQq: '23456789',
    stfEmail: 'wangwuwu@example.com',
    stfIdno: '110101198512171234',
    stfLocation: '广州市越秀区',
    dpNo: 'FIN001',
    stfLevel: '高级',
    stfCardno: '622202010112345690',
    stfStatus: '1'
  },
  {
    stfNo: '014',
    stfName: '赵六',
    stfSex: '女',
    stfTel: '13800138013',
    stfQq: '34567890',
    stfEmail: 'zhaoliu@example.com',
    stfIdno: '110101199212181234',
    stfLocation: '深圳市福田区',
    dpNo: 'IT002',
    stfLevel: '中级',
    stfCardno: '622202010112345691',
    stfStatus: '1'
  },
  {
    stfNo: '015',
    stfName: '孙七',
    stfSex: '男',
    stfTel: '13800138014',
    stfQq: '45678901',
    stfEmail: 'sunqi@example.com',
    stfIdno: '110101199002191234',
    stfLocation: '成都市锦江区',
    dpNo: 'IT001',
    stfLevel: '初级',
    stfCardno: '622202010112345692',
    stfStatus: '0'
  }
  ]);
</script>
<template>
  <v-row>
    <v-col cols="12" md="12">
      <v-card title="Table Demo">

        <v-data-table height="40rem" fixed-header>
          <thead>
          <tr style="white-space: nowrap;">
            <th class="text-uppercase">员工ID</th>
            <th>姓名</th>
            <th>性别</th>
            <th>电话号</th>
            <th>QQ</th>
            <th>邮箱</th>
            <th>身份证号</th>
            <th>住址</th>
            <th>部门</th>
            <th>级别</th>
            <th>工资卡号</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
          </thead>

          <tbody>
          <tr v-for="item in staff" style="white-space: nowrap;">
            <td>
              {{ item.stfNo }}
            </td>
            <td class="text-center">
              {{ item.stfName }}
            </td>
            <td class="text-center">
              {{ item.stfSex }}
            </td>
            <td class="text-center">
              {{ item.stfTel }}
            </td>
            <td class="text-center">
              {{ item.stfQq }}
            </td>
            <td class="text-center">
              {{ item.stfEmail }}
            </td>
            <td class="text-center">
              {{ item.stfIdno }}
            </td>
            <td class="text-center">
              {{ item.stfLocation }}
            </td>
            <td class="text-center">
              {{ item.dpNo }}
            </td>
            <td class="text-center">
              {{ item.stfLevel }}
            </td>
            <td class="text-center">
              {{ item.stfCardno }}
            </td>
            <td class="text-center">
              {{ item.stfStatus === "1" ? "在职" : "离职" }}
            </td>
            <td class="text-center" style="min-width: 200px;">
              <VBtn color="primary" @click="modifyStaff(item)">修改</VBtn>
              <VBtn color="error" @click="deleteStaff(item)" class="ml-5">停用</VBtn>
            </td>
          </tr>
          </tbody>
        </v-data-table>
      </v-card>
    </v-col>
  </v-row>
</template>
