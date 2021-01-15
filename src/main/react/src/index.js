import zhCN from 'antd/lib/locale/zh_CN';
import ReactDOM from 'react-dom';
import {BrowserRouter} from 'react-router-dom';
import App from './components/App';
import {ConfigProvider} from 'antd';

ReactDOM.render(
    (
        <ConfigProvider locale={zhCN}>
            <BrowserRouter>
                <App/>
            </BrowserRouter>
        </ConfigProvider>
    )
    , document.getElementById('root')
);
