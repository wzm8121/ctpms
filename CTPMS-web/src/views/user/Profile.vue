<template>
    <div>
        <VCard>
        <VTabs
            v-model="activeTab"
            show-arrows
            class="v-tabs-pill"
        >
            <VTab
                v-for="item in tabs"
                :key="item.icon"
                :value="item.tab"
            >
                <VIcon
                    size="20"
                    start
                    :icon="item.icon"
                />
                {{ item.title }}
            </VTab>
        </VTabs>

        <VWindow
            v-model="activeTab"
            class="mt-5 disable-tab-transition"
            :touch="false"
        >
            <!-- Account -->
            <VWindowItem value="account">
                <AccountSettingsAccount />
            </VWindowItem>

            <!-- Security -->
            <VWindowItem value="security">
                <AccountSettingsSecurity />
            </VWindowItem>
            
        </VWindow>
        </VCard>
    </div>
</template>

<script lang="ts" setup>
import { useRoute } from 'vue-router'
import AccountSettingsAccount from '@/views/user/account-settings/AccountSettingsAccount.vue'
import AccountSettingsNotification from '@/views/user/account-settings/AccountSettingsNotification.vue'
import AccountSettingsSecurity from '@/views/user/account-settings/AccountSettingsSecurity.vue'
import { ref } from 'vue';

const route = useRoute()

const activeTab = ref(route.params.tab)

// tabs
const tabs = [
    { title: 'Account', icon: 'ri-group-line', tab: 'account' },
    { title: 'Security', icon: 'ri-lock-line', tab: 'security' },
    { title: 'Notifications', icon: 'ri-notification-3-line', tab: 'notification' },
]
</script>