-- phpMyAdmin SQL Dump
-- version 4.0.9
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jul 18, 2014 at 08:09 PM
-- Server version: 5.6.14
-- PHP Version: 5.5.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `sdb_clinicas`
--

-- --------------------------------------------------------

--
-- Table structure for table `api_request`
--

CREATE TABLE IF NOT EXISTS `api_request` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `userId` int(11) NOT NULL,
  `token` text NOT NULL,
  `time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `request` text NOT NULL,
  `responseCode` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `article`
--

CREATE TABLE IF NOT EXISTS `article` (
  `articleId` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) NOT NULL,
  `categoryId` int(11) NOT NULL,
  `date` date NOT NULL,
  `title` varchar(255) NOT NULL,
  `content` text NOT NULL,
  `numberOfFavorites` int(11) NOT NULL DEFAULT '0',
  `pictureUrl` varchar(255) NOT NULL,
  PRIMARY KEY (`articleId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

--
-- Dumping data for table `article`
--

INSERT INTO `article` (`articleId`, `userId`, `categoryId`, `date`, `title`, `content`, `numberOfFavorites`, `pictureUrl`) VALUES
(1, 4, 1, '2014-07-08', 'Diabetes in México', 'MEXICO has long been a country that derives extraordinary pleasure from eating and drinking—and it hasn’t minded the consequences much either. Gordo or gorda, meaning “chubby”, is used by both wives and husbands as a term of endearment. Pudgy kids bear proudly the nickname gordito, as they tuck into snacks after school slathered with beans, cheese, cream and salsa.\r\n\r\nYour correspondent, having just arrived to live in Mexico City after more than a decade away, finds the increase in waistlines even more staggering than the increase in traffic. Mexico has become one of the most overweight countries on earth, even more so than the United States; a quarter of its men and a third of its women are obese. Indecorously, the country has even come up with figures on figures: the Mexican Diabetes Federation says that among women between 20 and 49, the average waistline is 91.1cm (35.9 inches), more than 10cm above the “ideal” size. Stores are now full of large- and extra large-sized clothing.\r\n\r\nTime was, a prominent girth may have been enviable proof of relative prosperity. Now, it is a serious health risk. At a conference here on April 9th it was estimated that more than 10m Mexicans, or almost a sixth of the adult population, suffer from diabetes, largely because of over-eating and increasingly sedentary lifestyles. Mexico has the sixth most cases of diabetes in the world.\r\n\r\nDiabetes is one of the top two causes of death in the country, alongside (and occasionally overlapping with) heart disease. The diabetes federation says that the illness kills 70,000 people a year. However, it gets far less attention than much less deadly diseases such as HIV/AIDS, not to mention organised crime (which is responsible for roughly 60,000 deaths in the past six years). “It could get to the point where we are literally eating ourselves to death,” says Jesper Holland of Novo Nordisk, a Danish health-care company that is a big supplier of insulin to Mexico.\r\n\r\nThe precise causes of the onslaught are hard to pin down. The prevalence of snacking on salty, fatty food and drinking sugar-heavy fizzy drinks appears to be a big part of the problem. Reforma, a national newspaper, reported on April 9th that fizzy drinks accounted for seven out of ten drinks sold in Mexico. There was a rise of more than 2% last year, despite growing pressure in Congress to slam “sin taxes” upon the drinks. On a per-head basis Mexicans drink more Coca-Cola than any other country.\r\n\r\nLack of exercise—all that traffic means many Mexicans commute for at least two hours a day—is another factor. Though the swanky parts of Mexico City now boast bicycle lanes, parks with exercise machines and graceful boulevards to run along, on the outskirts, where the health problem is gravest, there are few such amenities.\r\n\r\nMr Holland asserts that “economic growth” is a big cause of the illness, especially in developing countries where societies have grown more prosperous in the space of 20 years, compared with hundreds of years in some developed countries. That could be partly true: India and China also have acute diabetes problems. Mexico, however, has not grown faster than other countries in Latin America, and the poor left behind by economic growth are just as likely to snack badly as the more prosperous. What’s more, Mexican-Americans in the United States are almost twice as likely as non-Hispanic whites to be diagnosed with diabetes, which suggests there are powerful genetic factors at work, too.\r\n\r\nPerhaps for Mexicans the biggest problem is living next door to the United States, which means the fast food and super-sized culture has a particularly strong influence. So do the American food and drink giants who sell vast quantities south of the border and have already proved adept at fending off sin taxes and other forms of anti-obesity regulation in the United States.\r\n\r\nIn a country like Mexico where there is not much stigma attached to being overweight, there would probably be stiff opposition to regulating consumers’ behaviour, especially as measures such as higher taxes on soft drinks would fall disproportionately on the poor. Instead, the government should play up gluttony as a killer, as it does with cigarettes—especially in school, where a third of children are said to be obese—and literally scare people off their junk food. Diabetes provides that opportunity. Given Mexico’s extensive public health-care system, the state foots the bill for the sharply rising cost of diabetes treatment. There is huge public interest in giving it more prominence.', 0, 'http://cdn.static-economist.com/sites/default/files/imagecache/full-width/images/2013/04/blogs/americas-view/20130413_amp501.jpg'),
(2, 4, 2, '0000-00-00', 'How to Live a Healthy Life As a Diabetic', 'If you have diabetes, you''ll be looking into improving and maintaining your health for the long run. You control your diabetes successfully, by eating well, exercising and keeping informed about developments for better treatment. Your quality of life is also about finding ways to be happy, share with others and have fun in your life. While you''ve got a condition which will affect you medically, it is possible to start each day afresh and take control of your health rather than let it dictate your routine.\r\n\r\n1 Make an appointment to discuss your overall health with your trusted health team. \r\n\r\nThis is important, both so that you understand what will help you and you don''t feel alone dealing with this disease. In particular:\r\nAlways seek medical advice for any questions or concerns you may have.\r\nDo not let small things go unnoticed––even little changes can mean something significant and the sooner you bring it to the attention of your doctor, the better.\r\nIf you have not been following your recommended diet, or taking your medications as directed, you need to see your doctor.\r\n\r\n2Follow your recommended diet with care.\r\n\r\nYour doctor or dietitian should have given you a diet to follow; diet is key to maintaining wellness when you have diabetes. Every diabetic individual has differing needs, so it''s likely that your doctor has tailored the diet suggestion to your specific needs.\r\nIf you haven''t been given a recommended diet, ask for one.\r\nAsk questions about what special needs you have and where you can source healthful options from if they''re hard to obtain in your area.\r\nRemember to drink carefully too––many commercial and homemade drinks contain sugar and other additions that may spoil a carefully followed diet if not accounted for.\r\nA food diary can be helpful if you''re struggling to stay on track. This will let you see where you have food triggers (such as emotional eating when upset or eating sugary foods when tired, etc.) and allows you to plan ahead and prevent bad eating habits.\r\nLearn to read labels. Everyone should read nutrition labels on food but for diabetics, this is even more important.\r\n\r\n3 Know what is healthy to eat as a diabetic. \r\n\r\nThe American Diabetes Association recommends food that is healthy for all persons, whether diabetic or not, so it''s nice to know that you are eating for health generally, not just to control the diabetes. The Association''s recommended foods include:\r\nWhole grains, beans, noodles, and starchy vegetables (including unrefined potatoes): 6 or more total servings per day. (Breads and cereals should be limited and low in sodium, avoiding white flour.)\r\nFruit: 2-4 servings per day\r\nVegetables: 3-5 servings per day\r\nMeat, fish, and cheese: 2-3 servings per day\r\nMilk and yogurt: 2-3 servings per day\r\nFats, sweets, and alcohol: Small amounts (subject to your doctor''s recommendations)\r\nCondiments should be low in sodium and free of sugar. Check the labels of foods that have been cured, pickled, corned, marinated, smoked and canned.\r\nKeep abreast of changes in advice about food intake, as revisions do occur from time to time. Get updates in email format, talk to your doctor regularly and stay alert about nutritional discoveries for diabetics.', 0, 'http://www.wikihow.com/Image:Live-a-Healthy-Life-As-a-Diabetic-Step-2.jpg'),
(3, 4, 2, '2014-07-18', 'How to Live a Healthy Life As a Diabetic - Part 2', '1 Drink at least 6 to 8 cups of fluid daily. \r\n\r\nWhile tap water is your absolute best first choice, you can also consume tea, coffee, unflavored soda/mineral waters, diet drinks, artificially sweetened drink powders, low calorie drinks, etc., unless otherwise advised by your doctor. You may need to limit the intake of milk due to its natural milk sugars––ask your doctor for advice.\r\nDrinks to avoid or minimize completely include: sports drinks, sweetened soda drinks, flavored water and milk, tonic water and fruit juice.\r\n\r\n2 Include "treats" in your diet. \r\n\r\nBe sure to ask your health advisers about the role of treats in your diet and what sort of treats are permissible. While sugary confectionery and sugary baked items are now out, this doesn''t mean the end of enjoying sweet treats. There are plenty of good diabetic cookbooks both in stores and online that you can use to recreate sugar-free treats that still taste fantastic. Many health food stores stock diabetic-suitable sweets and treats too, so start hunting around for substitutes that will still "hit the spot" and keep your sweet tooth satisfied.\r\nA healthy small snack includes as much as a medium piece of fresh fruit, a small serving of yogurt, a few wholegrain crackers, small handful of nuts, or celery sticks with hummus, etc.\r\n\r\n3 Exercise regularly. \r\n\r\nAsk your doctor for the recommended amount of exercise in your case. Usually 20-30 minutes most days of the week is enough. Go for a walk with your mates, or maybe you like going to the gym. Different exercise works for different people, so experiment to find the exercise that you like most, after taking advice from your doctor.\r\nIf your weight prevents you from exercising, ask your doctor about water assisted exercise, such as aqua jogging, aquarobics or simply walking in a pool.', 0, 'http://www.wikihow.com/Image:Live-a-Healthy-Life-As-a-Diabetic-Step-6.jpg'),
(4, 4, 3, '2014-07-18', 'What diabetes type are you?', 'Diabetes is due to either the pancreas not producing enough insulin, or the cells of the body not responding properly to the insulin produced.[5] There are three main types of diabetes mellitus:\r\n\r\nType 1 DM results from the body''s failure to produce enough insulin. This form was previously referred to as "insulin-dependent diabetes mellitus" (IDDM) or "juvenile diabetes". The cause is unknown.[3]\r\nType 2 DM begins with insulin resistance, a condition in which cells fail to respond to insulin properly.[3] As the disease progresses a lack of insulin may also develop.[6] This form was previously referred to as non insulin-dependent diabetes mellitus (NIDDM) or "adult-onset diabetes". The primary cause is excessive body weight and not enough exercise.[3]\r\nGestational diabetes, is the third main form and occurs when pregnant women without a previous history of diabetes develop a high blood glucose level.', 0, 'http://villagepharmacyhampstead.com/wp-content/uploads/2014/06/type-2-diabetes-3.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `categories`
--

CREATE TABLE IF NOT EXISTS `categories` (
  `categoryId` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `description` text NOT NULL,
  `picture` varchar(255) NOT NULL,
  PRIMARY KEY (`categoryId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

--
-- Dumping data for table `categories`
--

INSERT INTO `categories` (`categoryId`, `title`, `description`, `picture`) VALUES
(1, 'Diet', 'Healthy eating habits are a cornerstone. Here you will have delicious recipes.', 'http://cdn1.bostonmagazine.com/wp-content/uploads/2014/01/diets-main.jpg'),
(2, 'Healthy Lifestyle', 'How to live with diabetes', 'http://imunews.imu.edu.my/wp-content/uploads/2013/11/healthy_lifestyile.jpg'),
(3, 'Diabetes', 'More information about the disease.', 'http://voetinformatie.nl/wp-content/uploads/2013/01/Diabetestips.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `favorites`
--

CREATE TABLE IF NOT EXISTS `favorites` (
  `articleId` int(11) NOT NULL,
  `userId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `favorites`
--

INSERT INTO `favorites` (`articleId`, `userId`) VALUES
(1, 4);

-- --------------------------------------------------------

--
-- Table structure for table `feedback`
--

CREATE TABLE IF NOT EXISTS `feedback` (
  `feedbackId` int(11) NOT NULL AUTO_INCREMENT,
  `date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `type` int(11) NOT NULL,
  `message` text NOT NULL,
  `url` varchar(255) NOT NULL,
  `userId` int(11) NOT NULL,
  PRIMARY KEY (`feedbackId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `feedback`
--

INSERT INTO `feedback` (`feedbackId`, `date`, `type`, `message`, `url`, `userId`) VALUES
(1, '2014-07-08 22:57:48', 1, 'I found this article...', 'www.google.com', 4);

-- --------------------------------------------------------

--
-- Table structure for table `review`
--

CREATE TABLE IF NOT EXISTS `review` (
  `reviewId` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) NOT NULL,
  `articleId` int(11) NOT NULL,
  `rating` tinyint(4) NOT NULL,
  `date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `comments` text NOT NULL,
  PRIMARY KEY (`reviewId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `review`
--

INSERT INTO `review` (`reviewId`, `userId`, `articleId`, `rating`, `date`, `comments`) VALUES
(1, 4, 1, 4, '2014-07-08 21:57:59', 'Interesting article');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `userId` int(11) NOT NULL AUTO_INCREMENT,
  `authToken` text,
  `name` varchar(255) NOT NULL,
  `age` datetime NOT NULL,
  `gender` tinyint(1) NOT NULL,
  `hasDiabetes` tinyint(1) NOT NULL,
  `role` int(11) DEFAULT NULL,
  `isClinicasMember` int(11) DEFAULT NULL,
  `accountId` int(11) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=9 ;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`userId`, `authToken`, `name`, `age`, `gender`, `hasDiabetes`, `role`, `isClinicasMember`, `accountId`, `email`, `password`) VALUES
(3, '', 'Guilherme', '0000-00-00 00:00:00', 1, 0, NULL, 0, NULL, '', 'test'),
(4, '', 'Guilherme', '0000-00-00 00:00:00', 1, 0, NULL, 0, NULL, '', 'test'),
(5, '', 'Arjun', '0000-00-00 00:00:00', 0, 0, NULL, NULL, NULL, '', ''),
(6, '', 'Arjun', '0000-00-00 00:00:00', 0, 0, NULL, NULL, NULL, 'arjun@scu.edu', ''),
(7, '', 'Arjun Ramesh', '0000-00-00 00:00:00', 0, 0, NULL, NULL, NULL, 'arjun@scu.edu', ''),
(8, '', 'Arjun', '0000-00-00 00:00:00', 0, 0, NULL, NULL, NULL, '', '');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
