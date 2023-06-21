import { GlobalUtil } from '@/commons/GlobalUtil';
import { AxiosUtil } from '@/utils/AxiosUtil';

/**
 *Api 基本类型
 *
 * @export
 * @class BaseApi
 */
export class BaseService {
    public static HTTP_SUCCESS_NET_CODE = 0; // 成功的请求
    protected token = GlobalUtil.getAdminToken();

}


