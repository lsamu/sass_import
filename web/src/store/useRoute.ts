export const useRouter = () => {
    const root = getCurrentInstance();
    const that = root.proxy;
    const router = that.$router;
    return router
}

export const useRoute = () => {
    const root = getCurrentInstance();
    const that = root.proxy;
    const route = that.$route;
    return route
}