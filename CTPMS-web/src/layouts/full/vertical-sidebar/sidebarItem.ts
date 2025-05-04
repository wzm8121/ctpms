import {
    ApertureIcon,
    CopyIcon,
    LayoutDashboardIcon,
    LoginIcon,
    MoodHappyIcon,
    TypographyIcon,
    UserPlusIcon,
    TableIcon,
    DeviceCameraPhoneIcon,
    UserIcon, CategoryIcon, BrandProducthuntIcon, CheckIcon, FilesIcon
} from 'vue-tabler-icons';

export interface menu {
    header?: string;
    title?: string;
    icon?: any;
    to?: string;
    chip?: string;
    chipColor?: string;
    chipVariant?: string;
    chipIcon?: string;
    children?: menu[];
    disabled?: boolean;
    type?: string;
    subCaption?: string;
}

const sidebarItem: menu[] = [
    { header: 'Home' },
    {
        title: 'Dashboard',
        icon: LayoutDashboardIcon,
        to: '/'
    },
    { header: 'Management' },
    {
        title: 'User',
        icon: UserIcon,
        to: '/management/user'
    },
    {
        title: 'Category',
        icon: CategoryIcon,
        to: '/management/category'
    },
    {
        title: 'Product',
        icon: BrandProducthuntIcon,
        to: '/management/product'
    },
    {
        title: 'ProductReview',
        icon: CheckIcon,
        to: '/management/productReview'
    },
    { header: 'Face' },
    {
        title: 'Register',
        icon: UserPlusIcon,
        to: '/face/register'
    },
    {
        title: 'Verify',
        icon: DeviceCameraPhoneIcon,
        to: '/face/verify'
    },
    { header: 'auth' },
    {
        title: 'Login',
        icon: LoginIcon,
        to: '/auth/login'
    },
    {
        title: 'Register',
        icon: UserPlusIcon,
        to: '/auth/register'
    },
    { header: 'Profile' },
    {
        title: 'Profile',
        icon: FilesIcon,
        to: '/user/profile'
    },
    {
        title: 'Sample Page',
        icon: ApertureIcon,
        to: '/sample-page'
    },
];

export default sidebarItem;
