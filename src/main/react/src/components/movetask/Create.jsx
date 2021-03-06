import {Button, Checkbox, Divider, Form, Input, notification, Popover, Select, Spin, Steps, DatePicker} from 'antd';
import React, {useEffect, useState} from 'react';
import moment from 'moment';
import {createBucket, getAllDtSourceName, getBucketLists} from '../datasource/service';
import {getContactLists} from '../pushconfig/service';
import {createMoveTask} from './service';
import {PlusOutlined} from '@ant-design/icons';

const {Step} = Steps;
const {Option} = Select;
const layout = {
    labelCol: {
        span: 4,
    },
    wrapperCol: {
        span: 16,
    },
};


const Create = (props) => {
    const [loading, setLoading] = useState(false);
    const [current, setCurrent] = useState(0);
    const [data, setData] = useState({});
    const [checked, setChecked] = useState(true);
    const [contactChecked, setContactChecked] = useState(false);
    const [dtsList, setDtsList] = useState([]);
    const [srcBucketList, setSrcBucketList] = useState([]);
    const [desBucketList, setDesBucketList] = useState([]);
    const [contactList, setContactList] = useState([]);
    const [showAddBucket, setShowAddBucket] = useState(false);
    const [newBucketName, setNewBucketName] = useState([]);

    const [form] = Form.useForm();
    useEffect(() => {
        setLoading(true)
        getAllDtSourceName()
            .then((response) => {
                const result = response.data.message;
                if (result === 'Success') {
                    setDtsList(response.data.data)
                } else {
                    notification['error']({
                        message: '通知',
                        description:
                            '获取数据源列表失败，请检查数据源配置',
                        duration: 2,
                    });
                }
                setLoading(false)
            });
    }, []);

    const prev = () => {
        setCurrent(current - 1);
    };

    const onChange = () => {
        setChecked(!checked)
    }
    const contactChange = () => {
        setContactChecked(!contactChecked)
    }

    const srcChange = (id) => {
        setLoading(true)
        form.setFieldsValue({srcBucket: undefined})
        setSrcBucketList([])
        getBucketLists({
            id: id
        }).then((response) => {
            const result = response.data.message;
            if (result === 'Success') {
                setSrcBucketList(response.data.data)
            } else {
                notification['error']({
                    message: '通知',
                    description:
                        '获取Bucket列表失败，请检查数据源配置',
                    duration: 2,
                });
            }
            setLoading(false)
        });
    }

    const desChange = (id) => {
        setLoading(true)
        form.setFieldsValue({desBucket: undefined})
        setDesBucketList([])
        getBucketLists({
            id: id
        }).then((response) => {
            const result = response.data.message;
            if (result === 'Success') {
                setDesBucketList(response.data.data)
                setShowAddBucket(true)
            } else {
                notification['error']({
                    message: '通知',
                    description:
                        '获取Bucket列表失败，请检查数据源配置',
                    duration: 2,
                });
            }
            setLoading(false)
        });
    }

    const onContactFocus = (id) => {
        setLoading(true)
        form.setFieldsValue({desBucket: undefined})
        setDesBucketList([])
        getContactLists({
            id: 42
        }).then((response) => {
            const result = response.data.message;
            if (result === 'Success') {
                setContactList(response.data.data)
            } else {
                notification['error']({
                    message: '通知',
                    description:
                        '获取联系人列表失败，请检查网络配置',
                    duration: 2,
                });
            }
            setLoading(false)
        });
    }

    const addItem = () => {
        const desId = form.getFieldValue('desId');
        createBucket({
            desId: desId,
            newBucketName: newBucketName,
        }).then((response) => {
            const result = response.data.message;
            if (result === 'Success') {
                setNewBucketName([])
                notification['success']({
                    message: '通知',
                    description:
                        '创建Bucket成功',
                    duration: 2,
                });
                setDesBucketList(response.data.data)
            } else {
                notification['error']({
                    message: '通知',
                    description:
                        '创建Bucket列表失败，请检查配置',
                    duration: 2,
                });
            }
        });
    }

    function range(start, end) {
        const result = [];
        for (let i = start; i < end; i++) {
            result.push(i);
        }
        return result;
    }


    const quit = () => {
        props.history.goBack();

    }

    const onNameChange = event => {
        setNewBucketName(event.target.value)
    }

    const srcOnFinish = values => {
        setData({...data, 'src': values, 'allMove': checked})
        setCurrent(current + 1);
    };

    const desOnFinish = values => {
        setData({...data, 'des': values})
        setCurrent(current + 1);
    };

    const onContactChange = values => {
        setData({...data, 'contact': values})
    };

    const onTimeChange = value => {
        setData({...data, 'time': value?.format('yyyy-MM-DD HH:mm:ss')})
    };


    const OnFinish = values => {
        const body = {...data, 'option': values}
        const [form] = Form.useForm();
        createMoveTask({
            srcId: body.src.srcId,
            srcBucket: body.src.srcBucket,
            fileNameStart: body.src.fileNameStart,
            fileNameEnd: body.src.fileNameEnd,
            desId: body.des.desId,
            desBucket: body.des.desBucket,
            allMove: body.allMove,
            taskName: body.option.taskName,
            sendMail: contactChecked,
            contact: body.contact,
            time: body.time
        })
            .then((response) => {
                const result = response.data.message;
                if (result === 'Success') {
                    notification['success']({
                        message: '通知',
                        description:
                            '创建迁移任务成功',
                        duration: 2,
                    });
                    props.history.push('/movetask/show');
                } else if (result === 'CreateRepeat') {
                    notification['error']({
                        message: '通知',
                        description:
                            '请不要重复使用迁移任务名称',
                        duration: 2,
                    });
                } else {
                    notification['error']({
                        message: '通知',
                        description:
                            '创建迁移任务失败，请检查配置',
                        duration: 2,
                    });
                }
            });
    };


    const steps = [
        {
            title: '选择初始数据源',
            content: (<Form
                {...layout}
                name="basic"
                form={form}
                onFinish={srcOnFinish}
            >
                <Form.Item
                    tooltip="选择源端数据源"
                    label="数据源"
                    name="srcId"
                    rules={[{required: true, message: '请选择数据源'}]}
                >
                    <Select
                        placeholder="请选择数据源"
                        allowClear
                        showSearch
                        optionFilterProp="children"
                        filterOption={(input, option) =>
                            option.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                        }
                        filterSort={(optionA, optionB) =>
                            optionA.children.toLowerCase().localeCompare(optionB.children.toLowerCase())
                        }
                        onChange={srcChange}
                    >
                        {
                            dtsList.map((item, index) =>
                                <Option key={index} value={item.id}>{item.dtSourceName}</Option>
                            )
                        }
                    </Select>
                </Form.Item>
                <Form.Item
                    tooltip="选择你所要迁移的Bucket"
                    name="srcBucket"
                    label="Bucket"
                    rules={[{required: true, message: '请选择Bucket'}]}>
                    <Select
                        placeholder="请选择Bucket"
                        allowClear
                        showSearch
                        optionFilterProp="children"
                        filterOption={(input, option) =>
                            option.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                        }
                        filterSort={(optionA, optionB) =>
                            optionA.children.toLowerCase().localeCompare(optionB.children.toLowerCase())
                        }
                    >
                        {
                            srcBucketList.map((item, index) =>
                                <Option key={index} value={item}>{item}</Option>
                            )
                        }
                    </Select>
                </Form.Item>

                <Form.Item label="全量迁移">
                    <Checkbox onChange={onChange} checked={checked}></Checkbox>
                </Form.Item>

                {!checked &&
                <div>
                    <Form.Item
                        label="请输入文件名前缀"
                        name="fileNameStart"
                    >
                        <Input placeholder="请输入文件名前缀"/>
                    </Form.Item>
                    <Form.Item
                        label="请输入文件名后缀"
                        name="fileNameEnd"
                    >
                        <Input placeholder="请输入文件名后缀"/>
                    </Form.Item>
                </div>
                }


                <Form.Item>
                    <Button htmlType="button" style={{margin: '0 10px 0 25%'}} onClick={() => quit()}>
                        取消
                    </Button>
                    <Button type="primary" htmlType="submit">
                        下一步
                    </Button>
                </Form.Item>
            </Form>),
        },
        {
            title: '选择目的数据源',
            content: (<Form
                {...layout}
                name="basic"
                form={form}
                onFinish={desOnFinish}
            >
                <Form.Item
                    tooltip="选择目的数据源"
                    label="数据源"
                    name="desId"
                    rules={[{required: true, message: '请选择数据源'}]}
                >
                    <Select
                        placeholder="请选择数据源"
                        allowClear
                        showSearch
                        optionFilterProp="children"
                        filterOption={(input, option) =>
                            option.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                        }
                        filterSort={(optionA, optionB) =>
                            optionA.children.toLowerCase().localeCompare(optionB.children.toLowerCase())
                        }
                        onChange={desChange}
                    >
                        {
                            dtsList.map((item, index) =>
                                <Option key={index} value={item.id}>{item.dtSourceName}</Option>
                            )
                        }
                    </Select>
                </Form.Item>


                <Form.Item
                    tooltip="选择你所需要迁移到的Bucket"
                    name="desBucket"
                    label="Bucket"
                    rules={[{required: true, message: '请选择Bucket'}]}
                >
                    <Select
                        placeholder="请选择Bucket"
                        allowClear
                        showSearch
                        optionFilterProp="children"
                        filterOption={(input, option) =>
                            option.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                        }
                        filterSort={(optionA, optionB) =>
                            optionA.children.toLowerCase().localeCompare(optionB.children.toLowerCase())
                        }
                        dropdownRender={menu => (
                            <div>
                                {menu}
                                {showAddBucket && <div>
                                    <Divider style={{margin: '4px 0'}}/>
                                    <div style={{display: 'flex', flexWrap: 'nowrap', padding: 8}}>
                                        <Input style={{flex: 'auto'}} value={newBucketName} onChange={onNameChange}/>
                                        <a
                                            style={{flex: 'none', padding: '8px', display: 'block', cursor: 'pointer'}}
                                            onClick={addItem}
                                        >
                                            <PlusOutlined/> 创建Bucket
                                        </a>
                                    </div>
                                </div>}
                            </div>
                        )}
                    >
                        {
                            desBucketList.map((item, index) =>
                                <Option key={index} value={item}>{item}</Option>
                            )
                        }
                    </Select>
                </Form.Item>


                <Form.Item>
                    <Button type="primary" htmlType="button" style={{margin: '0 10px 0 25%'}} onClick={prev}>
                        上一步
                    </Button>
                    <Button type="primary" htmlType="submit">
                        下一步
                    </Button>
                </Form.Item>
            </Form>),
        },
        {
            title: '参数设置',
            content: (<Form
                {...layout}
                name="basic"
                onFinish={OnFinish}
            >
                <Form.Item
                    tooltip="请输入迁移任务名称"
                    label="迁移任务名称"
                    name="taskName"
                    rules={[{required: true, message: '请输入迁移任务名称'}]}
                >
                    <Input placeholder="请输入迁移任务名称"/>
                </Form.Item>

                <Form.Item label="任务完成通知" name="sendMail">
                    <Checkbox onChange={contactChange} checked={contactChecked}></Checkbox>
                </Form.Item>

                {contactChecked &&
                <Form.Item
                    tooltip="在推送配置中,可创建联系人"
                    name="email" label="联系人" rules={[{required: true, message: '请选择联系人'}]}>
                    <Select
                        placeholder="请选择联系人"
                        allowClear
                        onFocus={onContactFocus}
                        onChange={onContactChange}
                    >
                        {
                            contactList.map((item, index) =>
                                <Option key={index} value={item.contactEmail}>{item.contactName}</Option>
                            )
                        }
                    </Select>
                </Form.Item>
                }


                <Form.Item
                    tooltip="可以指定开始启动的时间"
                    name="time"
                    label="定时执行"
                >
                    <DatePicker
                        format="YYYY-MM-DD HH:mm:ss"
                        showTime
                        onChange={onTimeChange}
                    />
                </Form.Item>


                <Form.Item>
                    <Button type="primary" htmlType="submit" style={{margin: '0 10px 0 25%'}}>
                        确定
                    </Button>
                    <Button type="primary" htmlType="button" onClick={prev}>
                        上一步
                    </Button>
                </Form.Item>
            </Form>),
        },
    ];


    const customDot = (dot, {status, index}) => (
        <Popover
            content={
                <span>
              step {index}
            </span>
            }
        >
            {dot}
        </Popover>
    );


    return <Spin size="large" spinning={loading}>
        <Steps forcerender="true" progressDot={customDot} current={current}>
            {steps.map(item => (
                <Step key={item.title} title={item.title}/>
            ))}
        </Steps>
        <div className="steps-content">{steps[current].content}
            <div className="steps-action">
            </div>
        </div>
    </Spin>
}


export default Create;