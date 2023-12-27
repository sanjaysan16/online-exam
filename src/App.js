import AddExam from "./Component/AddExam";
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap.bundle.min";
import 'bootstrap-icons/font/bootstrap-icons.css';
import './App.css';
import { BrowserRouter, Routes, Route } from 'react-router-dom'
import Welcome from './Component/Welcome'
import ExamDashBord from './Component/ExamDashBord';
import AddTopic from './Component/AddTopic'
import TopicsDisplay from './Component/TopicsDisplay'

import AddQuestion from './Component/AddQuestion'
import Header from './Component/Header'
import Footer from './Component/Footer'
import ViewQuestion from './Component/ViewQuestion'
import UpdateQuestion from './Component/UpdateQuestion'

import ViewUsers from './Component/ViewUsers'
import UserExamMap from './Component/UserExamMap'
import AdminView from "./Component/AdminView";



function App() {
  return (
    <>

      <Header />

      <BrowserRouter>
        <Routes>

          <Route path='/' element={<Welcome />} />
          <Route path='/admin-view' element={<AdminView/>}/>
          <Route path='/add-exam' element={<AddExam />} />
          <Route path='/add-topic' element={<AddTopic />} />
          <Route path='/view-topic' element={<TopicsDisplay/>}/>
          <Route path='/add-question' element={<AddQuestion />} />
          <Route path='/view-question' element={<ViewQuestion />} />
          <Route path='/update-question' element={<UpdateQuestion />} />
         
          <Route path='/view-exam' element={<ExamDashBord/>}/>
          
          <Route path='/view-user' element={<ViewUsers/>}/>
          <Route path='/user-exam-mapping' element={<UserExamMap/>}/>
        </Routes>
      </BrowserRouter>
      <Footer />
    </>
  );
}

export default App;
