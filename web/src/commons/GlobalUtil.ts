/***
 * 全局配置文件
 */
export class GlobalUtil {

    static config;

    public static Set(config){
        this.config = config;
    }

    public static Get(){
        return this.config;
    }

    /**
     * Api地址
     */
    public static Url(): string {
        let site_url = "";
        try {
            site_url = SITE_API_URL;
        } catch (error) {
            if (process.env.NODE_ENV !== "production") {
                site_url = "https://mall.v5soft.com";
            }
            // console.log(error);
        }
        return site_url;
    }

    //获取站点名称
    public static GetSiteName(): string {
        let site_name = "后台管理系统";
        try {
            site_name = document.title;// SITE_TITLE;
        } catch (error) {
            //console.log(error);
        }
        return site_name;
    }

    /**
     * 多图列表分解
     */
    public static MorePicToSinglePicList(morepic: string) {
        let list = morepic.split("||");
        let ourPicList: any[] = [];
        list.forEach(element => {
            let a = element.split(",");
            let ourPic: any = {}
            ourPic.name = a[0];
            ourPic.url = a[1];
            ourPic.default = Number(a[2]) || 0;
            ourPicList.push(ourPic);
        });
        return ourPicList;
    }

    //设置用户登录模块信息
    public static setModule(result: any) {
        localStorage.setItem("admin_module_login", JSON.stringify(result));
    }

    //获取用户登录模块信息
    public static getModule() {
        let itemInfo = localStorage.getItem("admin_module_login");
        let staffLoginInfo = JSON.parse(itemInfo);
        return staffLoginInfo as any;
    }

    //设置后台Token
    public static setAdminToken(val: string) {
        localStorage.setItem("admin_login_token", val);
    }

    //获取后台Token
    public static getAdminToken(): string {
        return localStorage.getItem("admin_login_token") as string;
    }

    //设置前台用户Token
    public static setUserToken(val: string) {
        localStorage.setItem("user_login_token", val);
    }

    //获取前台用户Token
    public static getUserToken(): string {
        return localStorage.getItem("user_login_token") as string;
    }

}
