import { Component, Vue, Prop } from "vue-property-decorator";
import { BaseResult } from "@/commons/BaseModel";
import { GlobalUtil } from "./GlobalUtil";

export default class BaseVue extends Vue {
  /**
   * 根据Key获取实例
   * @param key
   */
  protected getInstanceByKey<T>(key: string) {
    let instance = (this.$refs[key] as any) as T;
    return instance;
  }

  public uploadUrl = GlobalUtil.GetSiteName() + "/api/admin/tools/upload"; //图片上传接口

  /**
   * 标题
   */
  public title: string = "";

  created() {
    console.log("created");
  }

  mounted() {
    console.log("mounted");
  }

  /**
   * 获取路由参数
   * @param key
   */
  public getParam(key: string): string {
    const param = this.$route.params[key];
    if (param == undefined || param == null) {
      return "";
    }
    return param as string;
  }

  //设置父组件导航
  public setNav(name: string, op: string) {
    this.$emit("navchange", name, op);
  }

  /**
   * 弹窗
   * @param callback
   * @param content
   * @param title
   */
  public alert(
    callback: () => void,
    content: string = "处理成功!",
    title: string = "提示"
  ) {
    this.$alert(content, title, {
      confirmButtonText: "确定",
      callback: callback,
    });
  }

  /**
   *
   * @param callback
   * @param content
   */
  public toast(callback: () => void, content: string = "处理成功!") {
    this.$message({
      message: content,
      type: "success",
      onClose: callback,
    });
  }
  /**
   *
   * @param callback1
   * @param callback2
   * @param content
   * @param title
   */
  public confirm(
    callback1: () => void,
    callback2: () => void,
    content: string = "处理成功!",
    title: string = "提示"
  ) {
    this.$confirm(content, title, {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    })
      .then(callback1)
      .catch(callback2);
  }

  /**
   * 跳转
   */
  public redirect(path: string) {
    this.$router.push({ path: path });
  }

  //后退
  public goback() {
    this.$router.back();
  }

  /**
   * 验证表单
   * @param refFormName 表单引用名称
   * @param callback 数据回调
   * @param callback_result 回调结果
   */
  public validateForm(
    refFormName: string = "form",
    callback: () => Promise<BaseResult>,
    callback_result: (flag: boolean) => void = null
  ) {
    (this.$refs[refFormName] as any).validate(async (valid) => {
      if (valid) {
        let d = await callback();
        if (d.code == 0) {
          this.$alert(d.msg, "提示", {
            confirmButtonText: "确定",
            callback: (action) => {
              callback_result(true);
            },
          });
        } else {
          this.$alert(d.msg, "提示", {
            confirmButtonText: "确定",
            callback: (action) => {
              callback_result(false);
            },
          });
        }
      }
    });
  }
  //验证表单
  public validateFormBlank(refFormName: string = "form", callback: () => void) {
    (this.$refs[refFormName] as any).validate(async (valid) => {
      if (valid) {
        callback();
      }
    });
  }

  /**
   * 数据删除
   */
  public dataDelete(
    callback: () => Promise<BaseResult>,
    callback_result: (flag: boolean) => void = null
  ) {
    this.$confirm("此操作将永久删除该项, 是否继续?", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    })
      .then(async () => {
        let d = await callback();
        if (d.code == 0) {
          this.$message({
            type: "success",
            message: "删除成功!",
          });
          if (callback_result != null) callback_result(true);
        } else {
          this.$message({
            type: "error",
            message: "处理失败!" + d.code + "::" + d.msg,
          });
          if (callback_result != null) callback_result(false);
        }
      })
      .catch(() => {
        this.$message({
          type: "info",
          message: "已取消删除",
        });
      });
  }

  //Tabs选项卡
  public refTab() {
    if (this["$tabs"] == null) {
      return null;
    }
    return this["$tabs"] as any;
  }
}
