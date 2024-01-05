import AddExam from "./Component/examSectionContainer/AddExam";
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap.bundle.min";
import 'bootstrap-icons/font/bootstrap-icons.css';
import './App.css';
import { BrowserRouter, Routes, Route } from 'react-router-dom'
import Welcome from './Component/Welcome'
import ExamDashBord from './Component/examSectionContainer/ExamDashBord';
import AddTopic from './Component/topicSectionComtainer/AddTopic'
import TopicsDisplay from './Component/topicSectionComtainer/TopicsDisplay'

import AddQuestion from './Component/questionSectionContainer/AddQuestion'
import Header from './Component/headerSectionContainer/Header'
import Footer from './Component/footerSectionContainer/Footer'
import ViewQuestion from './Component/questionSectionContainer/ViewQuestion'
import UpdateQuestion from './Component/questionSectionContainer/UpdateQuestion'

import ViewUsers from './Component/adminSectionContainer/ViewUsers'
import UserExamMap from './Component/adminSectionContainer/UserExamMap'
import AdminView from "./Component/adminSectionContainer/AdminView";
import UserDashBoard from "./Component/userSectionContainer/UserDashBoard";
import DashBord from "./Component/DashBordContainerSection/DashBord";



function App() {
  return (
    <>

      <Header />

      <BrowserRouter>
        <Routes>
          
          <Route path='/' element={<Welcome />} />
          <Route path='/admin-view' element={<AdminView/>}/>
          <Route path='/dash-bord' element={<DashBord/>}/>
          <Route path='/add-exam' element={<AddExam/>} />
          <Route path='/add-topic' element={<AddTopic/>} />
          <Route path='/view-topic' element={<TopicsDisplay/>}/>
          <Route path='/add-question' element={<AddQuestion/>} />
          <Route path='/view-question' element={<ViewQuestion/>} />
          <Route path='/update-question' element={<UpdateQuestion/>} />
         
          <Route path='/view-exam' element={<ExamDashBord/>}/>
          
          <Route path='/view-user' element={<ViewUsers/>}/>
          <Route path='/user-exam-mapping' element={<UserExamMap/>}/>
          <Route path='/user-dashboard' element={<UserDashBoard/>}/>
        </Routes>
      </BrowserRouter>
      <Footer />
    </>
  );
}

export default App;
