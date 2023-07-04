<template>
    <div>
        <el-row>
            <el-col style="padding:5px">
                <el-alert type="warning" :closable="false">
                    <template slot='title'>
                        <div class="iconSize">需要注意的事项:</div>
                        <div class="iconSize">1、登录后请及时检查对应的姓名和手机号是否正确</div>
                        <div class="iconSize">2、卡片式每页显示一个问题，列表式一页显示全部的问题</div>
                        <div class="iconSize">3、评测问卷提交保存后就无法再次做答了，请慎重对待每一个问题</div>
                    </template>
                </el-alert>
            </el-col>
        </el-row>

        <el-row>
            <el-col :span="16" style="padding:5px">
                <el-button type="primary" icon="el-icon-plus" @click="handleNew">新建</el-button>
                <el-button type="danger" icon="el-icon-delete" @click="handleDelete">删除</el-button>
            </el-col>
            <el-col :span="8" style="padding:5px;">
                <div style="float: right;">
                    <el-button icon="el-icon-refresh" type="primary" circle></el-button>
                    <el-button icon="el-icon-question" type="primary" circle></el-button>
                </div>
            </el-col>
        </el-row>
        <el-row>
            <el-col style="padding:5px">
                <el-table :data="thatOption.data" style="width: 100%" stripe border>
                    <el-table-column type="selection" width="55">
                    </el-table-column>
                    <el-table-column prop="date" label="编码" width="180">
                    </el-table-column>
                    <el-table-column prop="name" label="物理表名" width="180">
                    </el-table-column>
                    <el-table-column prop="address" label="显示名">
                    </el-table-column>
                    <el-table-column prop="address" label="备注">
                    </el-table-column>
                    <el-table-column fixed="right" label="操作" width="240">
                        <template #default="scope">
                            <el-button type="text" size="small" icon="el-icon-edit"
                                @click="handleEdit(scope.row)">编辑</el-button>
                            <el-button type="text" size="small" icon="el-icon-edit">模板</el-button>
                            <el-button type="text" size="small" icon="el-icon-edit">导入</el-button>
                            <el-button type="text" size="small" icon="el-icon-edit">导出</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </el-col>
        </el-row>
        <el-row>
            <el-col style="padding:5px">
                <el-pagination background :page-sizes="[100, 200, 300, 400]" :page-size="100"
                    layout="total, sizes, prev, pager, next, jumper" :total="400">
                </el-pagination>
            </el-col>
        </el-row>

        <el-dialog title="新建" :visible.sync="dialogOption.visible" width="70%">
            <el-form ref="form" :model="formOption.form" label-width="80px">
                <el-form-item label="数据表">
                    <el-input v-model="formOption.form.name"></el-input>
                </el-form-item>
                <el-form-item label="显示表">
                    <el-input v-model="formOption.form.name"></el-input>
                </el-form-item>
                <el-form-item label="备注">
                    <el-input type="textarea" v-model="formOption.form.name"></el-input>
                </el-form-item>
                <el-row>
                    <el-col>
                        <el-button type="primary" icon="el-icon-plus" @click="handleDataInsert">添加</el-button>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col>
                        <el-table :data="formOption.form.data" style="width: 100%;" height="300" stripe border>
                            <el-table-column prop="date" label="数据表列">
                                <template #default="scope">
                                    <el-input v-model="scope.row.name"></el-input>
                                </template>
                            </el-table-column>
                            <el-table-column prop="name" label="Excel列" width="180">
                                <template #default="scope">
                                    <el-input v-model="scope.row.name"></el-input>
                                </template>
                            </el-table-column>
                            <el-table-column prop="address" label="是否唯一">
                                <template #default="scope">
                                    <el-input v-model="scope.row.name"></el-input>
                                </template>
                            </el-table-column>
                            <el-table-column prop="address" label="关联表">
                                <template #default="scope">
                                    <el-input v-model="scope.row.name"></el-input>
                                </template>
                            </el-table-column>
                            <el-table-column prop="address" label="关联字段">
                                <template #default="scope">
                                    <el-input v-model="scope.row.name"></el-input>
                                </template>
                            </el-table-column>
                            <el-table-column prop="address" label="操作" header-align="center" align="center">
                                <template #default="scope">
                                    <el-button type="danger" icon="el-icon-edit">删除</el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                    </el-col>
                </el-row>

            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="dialogOption.visible = false">取 消</el-button>
                <el-button type="primary" @click="dialogOption.visible = false">确 定</el-button>
            </span>
        </el-dialog>

    </div>
</template>
<script lang="ts" setup>
import { Row } from 'element-ui'


const thatOption = reactive({
    data: [{
        date: '2016-05-02',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1518 弄'
    }, {
        date: '2016-05-04',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1517 弄'
    }, {
        date: '2016-05-01',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1519 弄'
    }, {
        date: '2016-05-03',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1516 弄'
    }]
})

const dialogOption = reactive({
    visible: false
})

const handleNew = () => {
    dialogOption.visible = true
}

const handleDelete = () => {

}

const handleEdit = (row) => {
    dialogOption.visible = true;
}

const formOption = reactive({
    form: { data: [] } as any
})

const handleDataInsert = () => {
    formOption.form.data.push({

    })
}
</script>
