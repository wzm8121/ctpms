<script setup lang="ts">
import { PowerIcon } from 'vue-tabler-icons';
</script>

<template>
    <v-sheet rounded="md" color="lightprimary" class="ExtraBox hide-menu mx-3 px-6 pb-5 pt-4">
        <div class="d-flex align-center">
            <div class="pr-1">
                <h6 class="text-h6 text-10 mb-2">Welcome Home</h6>
                <v-btn to="/" size="small" color="primary" flat>回到主页</v-btn>
            </div>
            <div class="mt-n7 ml-2">
                <img src="@/assets/images/background/rocket.png" />
            </div>
        </div>
    </v-sheet>
</template>
<style lang="scss">
.ExtraBox {
    position: relative;
}
.line-height-none {
    line-height: normal;
}
</style>
