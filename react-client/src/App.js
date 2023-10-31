import Header from "./components/Header";
import HeroSection from "./components/HeroSection";
import AboutUsSection from "./components/AboutUsSection";
import ClientSection from "./components/ClientsSection";
import StatsCounter from "./components/StatsCounter";
import OurServices from "./components/OurServices";
import Testimonials from "./components/Testimonials";
import AskQuestions from "./components/AskQuestions";
import RecentThings from "./components/RecentThings";
import Footer from "./components/Footer";

function App() {
  return (
    <div>
      <Header />
      <HeroSection />
      <main id="main">
        <AboutUsSection />
        <ClientSection />
        <StatsCounter />
        <OurServices />
        <Testimonials />
        <AskQuestions />
        <RecentThings />
      </main>
      <Footer />

      <a
        href="#"
        className="scroll-top d-flex align-items-center justify-content-center"
      />
      <i className="bi bi-arrow-up-short"></i>

      {/* <div id="preloader"></div> */}
    </div>
  );
}

export default App;
