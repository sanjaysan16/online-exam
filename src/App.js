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
import ReportCard from "./Component/adminSectionContainer/ReportCard";
import Result from "./Component/userSectionContainer/Result";
import UserAttempt from "./Component/userSectionContainer/UserAttempt";
import ExamInstruction from "./Component/userSectionContainer/ExamInstruction";
import TakingExam from "./Component/userSectionContainer/TakingExam";
import ExamDashBoardForUser from "./Component/userSectionContainer/ExamDashBoardForUser";
import ResultPageForAdmin from "./Component/adminSectionContainer/ResultPageForAdmin";
import ResultPageForAdminAndStudent from "./Component/adminSectionContainer/ResultPageForAdminAndStudent";



function App() {
  return (
    <>

      <Header />

      <BrowserRouter>
        <Routes>

          <Route path='/' element={<Welcome />} />
          <Route path='/admin-view' element={<AdminView />} />
          <Route path='/dash-bord' element={<DashBord />} />
          <Route path='/add-exam' element={<AddExam />} />
          <Route path='/add-topic' element={<AddTopic />} />
          <Route path='/view-topic' element={<TopicsDisplay />} />
          <Route path='/add-question' element={<AddQuestion />} />
          <Route path='/view-question' element={<ViewQuestion />} />
          <Route path='/update-question' element={<UpdateQuestion />} />
          <Route path='/result-page-for-admin' element={<ResultPageForAdmin />} />
          <Route path='/result-page-for-admin-and-student' element={<ResultPageForAdminAndStudent />} />


          <Route path='/view-exam' element={<ExamDashBord />} />
          <Route path='/view-result' element={<ReportCard />} />
          <Route path='/view-user' element={<ViewUsers />} />
          <Route path='/result' element={<Result />} />
          <Route path='/exam-instruction' element={<ExamInstruction />} />
          <Route path='/taking-exam' element={<TakingExam />} />
          <Route path='/user-exam-dashboard' element={<ExamDashBoardForUser />} />
          <Route path='/user-attempt' element={<UserAttempt />} />
          <Route path='/user-exam-mapping' element={<UserExamMap />} />
          <Route path='/user-dashboard' element={<UserDashBoard />} />
        </Routes>
      </BrowserRouter>
      <Footer />
    </>
  );
}

export default App;

// import {useRef, useEffect} from 'react';

// const App = () => {
//   const ref = useRef(null);

//   useEffect(() => {
//     const handleClick = () => {
//       console.log('Button clicked');

//       console.log('bobbyhadz.com');
//     };

//     const element = ref.current;
//     element.addEventListener('click', handleClick);

//     return () => {
//       element.removeEventListener('click', handleClick);
//     };
//   }, []);

//   return (
//     <div>
//       <button ref={ref}>Click</button>
//     </div>
//   );
// };

// export default App;
