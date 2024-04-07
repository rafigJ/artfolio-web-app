import { LockOutlined, UserOutlined } from '@ant-design/icons';
import { Button, Form, Input, Typography } from 'antd';
import './LoginFormStyles.css'
import { useNavigate } from 'react-router-dom';

const LoginForm = () => {
    const navigate = useNavigate()
    const onFinish = (values: any) => {
        console.log('Received values of form: ', values);
      };

    return (
        <div className='login-form-container'>
          <Form
            name="normal_login"
            className='login-form'
            initialValues={{ remember: true }}
            onFinish={onFinish}
          >
          <Typography.Title level={3} className='login-title'>Авторизация</Typography.Title>
          <Form.Item
            name="email"
            rules={[{ required: true, message: 'Введите электронную почту!' }]}
          >
            <Input prefix={<UserOutlined className="site-form-item-icon" />} placeholder="Электронная почта" />
          </Form.Item>

          <Form.Item
            name="password"
            rules={[{ required: true, message: 'Введите пароль!' }]}
          >
            <Input.Password
              prefix={<LockOutlined className="site-form-item-icon" />}
              type="password"
              placeholder="Пароль"
            />
          </Form.Item>
          <Form.Item>
            <a className="login-form-forgot" onClick={() => navigate('/forgotpassword')}>
              Забыли пароль
            </a>
          </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit" className="login-form-button">
              Войти
            </Button>
            Нет аккаунта? <a onClick={() => navigate('/register')}>Зарегистрироваться!</a>
          </Form.Item>
          </Form>
        </div>
        
  );
};

export default LoginForm;