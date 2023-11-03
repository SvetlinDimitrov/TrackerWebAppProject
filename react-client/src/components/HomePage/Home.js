import AboutUsSection from "./AboutUsSection";
import ClientSection from "./ClientsSection";
import HeroSection from "./HeroSection";
// import StatsCounter from "./StatsCounter";
import OurServices from "./OurServices";
// import Testimonials from "./Testimonials";
import AskQuestions from "./AskQuestions";
// import RecentThings from "./RecentThings";

const Home = () => {
  return (
    <>
      <HeroSection />
      <main id="main">
        <AboutUsSection />
        <ClientSection />
        {/* <StatsCounter /> */}
        <OurServices />
        {/* <Testimonials /> */}
        <AskQuestions />
        {/* <RecentThings /> */}
      </main>
    </>
  );
};

export default Home;
