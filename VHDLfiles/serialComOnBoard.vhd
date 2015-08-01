library ieee;
use ieee.std_logic_1164.all;
use ieee.std_logic_arith.all;
use ieee.std_logic_unsigned.all;

entity main is
port(
	clk             :in std_logic;
	pushButtonIn    :in std_logic;
	reset_n			:in std_logic;
	clockV			:out std_logic;
	
	dataOut			:out std_logic;
	clkOut			:out std_logic;
	
	dataIn			:in std_logic;
	clkIn			:in std_logic;
	LEDs			:out std_logic_vector(7 downto 0);
	LEDsNotUsed     :out std_logic_vector(3 downto 0);
	--outputted so it can be sampled
	dataInComing:out std_logic
	);
end main;

architecture behavior of main is

---------------transmitter constants--------------------------------------------
constant count_19ms     		:std_logic_vector(24 downto 0) := "0000010010010011111000000";
constant count_19msAnd1CLK   	:std_logic_vector(24 downto 0) := "0000010010010011111000001";

constant byteAt1MHz	  		  	:std_logic_vector(8 downto 0)  := "100001111";
constant startCounterData     	:std_logic_vector(8 downto 0)  := "000010000";
constant defaultDataByte      	:std_logic_vector(7 downto 0)  := "10101010";

---------------transmitter signals--------------------------------------------
signal tx_data_strobe    		 :std_logic;
signal inc_data_strobe  		 :std_logic;
signal load_data_strobe			 :std_logic;
signal tx_data          		 :std_logic_vector(7 downto 0);
signal dataSR        		 	 :std_logic_vector(7 downto 0);
signal counterData				 :std_logic_vector(8 downto 0);
signal dataOutFF				 :std_logic;

---------------receiver signals--------------------------------------------
constant byteCount               :std_logic_vector(3 downto 0) := "1000";

signal syncInClkFF					 :std_logic_vector(2 downto 0);
signal clkIn_sync				 :std_logic;
signal dataIncomingSR			 :std_logic_vector(7 downto 0);
signal dataHoldingFF			 :std_logic_vector(7 downto 0);
signal counterInByte			 :std_logic_vector(3 downto 0);
signal signalFF					 :std_logic_vector(1 downto 0);
signal inClkNegEdg               :std_logic;

--push button debouncer signals---------------------------------------
signal pushButtonInFF			 :std_logic_vector(2 downto 0);
signal triggerPushButtonSignals  :std_logic;
signal pushButtonSignalFF		 :std_logic_vector(2 downto 0);
signal trigDBcounter             :std_logic;
signal counterDB				 :std_logic_vector(24 downto 0);

begin

--for testing purposes
dataInComing <= dataInComingSR(0);

--clockV supplies power to the clock oscillator and LEDsNotUsed turns off said LEDs
clockV <= '1';	
LEDsNotUsed <= (others => '1');
---------------------------de-bouncer for tx_data push button ---------------------------------------------------
---------------------------input: pushButtonIn ------------------------------------------------------------------
---------------------------output: tx_data_strobe, inc_data_strobe, load_data_strobe-----------------------------




process(reset_n,clk,pushButtonIn)
begin
	if(reset_n = '0')then
		pushButtonInFF <= (others => '0');
	
	elsif(clk'event AND clk = '1')then
--		if(pushButtonIn = '1')then
--			pushButtonInFF(0) <= '1';
			pushButtonInFF(0) <= pushButtonIn;
			pushButtonInFF(2 downto 1) <= pushButtonInFF(1 downto 0);
		
		-- else
			-- pushButtonInFF(0) <= '0';
			-- pushButtonInFF(2 downto 1) <= pushButtonInFF(1 downto 0);
		
		--end if;		
	end if;
end process;

trigDBcounter <= '1'when(pushButtonInFF(2) = '0' AND pushButtonInFF(1) = '1')else'0';

-- counter for deBouncer------------------------------------
process(reset_n,clk,counterDB)
begin
	if(reset_n = '0')then
		counterDB <= (others => '0');
	
	elsif(clk'event AND clk = '1')then
		if(trigDBcounter = '1')then
			counterDB <= (others => '0');
		
		elsif(counterDB /= count_19msAnd1CLK)then
			counterDB <= counterDB + 1;
			
		end if;
	end if;
end process;

--produces the signal to trigger the following flip flops
process(reset_n,clk,counterDB)
begin
	if(reset_n = '0')then
		triggerPushButtonSignals <= '0';
		
	elsif(clk'event AND clk = '1')then
		if(counterDB = count_19ms)then
			triggerPushButtonSignals <= pushButtonInFF(2);
		 else
			triggerPushButtonSignals <= '0';
			
		end if;	
	end if;
end process;

--pushButtonSignalFF produce the signals the following logic
process(reset_n,clk,triggerPushButtonSignals)
begin
	if(reset_n = '0')then
		pushButtonSignalFF <= (others => '0');
	
	elsif(clk'event AND clk = '1')then
		pushButtonSignalFF(0) <= triggerPushButtonSignals;
		pushButtonSignalFF(2 downto 1) <= pushButtonSignalFF(1 downto 0);

	end if;
end process;

--Logic produces the 3 signals for loading transmitting and incrementing the data byte
load_data_strobe <= '1' when(pushButtonSignalFF(0) = '1')else'0';
tx_data_strobe <= '1' when(pushButtonSignalFF(1) = '1')else'0';
inc_data_strobe <= '1' when(pushButtonSignalFF(2) = '1')else'0';


------------------transmitter portion of the code -----------------------------------------------------------------
------------------input: load_data_strobe, tx_data_strobe, inc_data_strobe ----------------------------------------
------------------output: dataOut, clkOut -------------------------------------------------------------------------

--increments data register to the next value or resets to known value of 170_10 10101010_2
process(reset_n,clk,inc_data_strobe)
begin
	if(reset_n = '0')then
		tx_data <= defaultDataByte;
	
	elsif(clk'event AND clk = '1' AND inc_data_strobe = '1')then
		tx_data <= tx_data + 1;
	
	end if;
end process;

--counter produces the output clk and data signals then loads the new data into the shift register 
process(reset_n,clk,tx_data_strobe,counterData)
begin
	if(reset_n = '0')then
		counterData <= byteAt1MHz;
	
	elsif(clk'event AND clk = '1')then
		if(tx_data_strobe = '1')then
			counterData <= startCounterData;
		
		elsif(counterData /= byteAt1MHz)then
			counterData <= counterData + 1;
		
		end if;	
	end if;
end process;


-- process loads and shifts the shift registers for outputting data
process(reset_n,clk,load_data_strobe,counterData(4 downto 0))
begin
	if(reset_n = '0')then
		dataSR <= (others => '0');
		dataOutFF <= '0';
	
	elsif(clk'event AND clk = '1')then
		if(load_data_strobe = '1')then
			dataSR <= tx_data;
		
		elsif(counterData(4 downto 0) = startCounterData(4 downto 0))then
			dataSR(0) <= '0';
			dataSR(7 downto 1) <= dataSR(6 downto 0);
			dataOutFF <= dataSR(7);
			
		end if;
	end if;		
end process;

--produces the clk out signal
process(reset_n,clk)
begin
	if(reset_n = '0')then
		clkOut <= '0';
	
	elsif(clk'event AND clk = '1')then
		clkOut <= counterData(4);
		
	end if;
end process;

--the data out signal
dataOut <= dataOutFF when(counterData /= byteAt1MHz)else '0';

--------------------------------------receiver portion of the code-------------------------------
--------------------------------------input: dataIn, clkIn --------------------------------------
--------------------------------------output: LEDs ----------------------------------------------

--Flip Flops synchronize the incoming clock signal to the on board clock
process(reset_n,clk)
begin
	if(reset_n = '0')then
		syncInClkFF <= (others => '0');
	
	elsif(clk'event AND clk = '1')then
		syncInClkFF(0) <= clkIn;
		syncInClkFF(2 downto 1) <= syncInClkFF(1 downto 0);
	
	end if;
end process;

--pulse that signals a negative clock edge on the incoming clk
inClkNegEdg <= '1'when(syncInClkFF(2) = '1' AND syncInClkFF(1) = '0')else '0';

-- incoming data shift register data is sampled on the negative edge of the incoming clk
process(reset_n,clk,inClkNegEdg)
begin
	if(reset_n = '0')then
		dataIncomingSR <= (others => '0');
	
	elsif(clk'event AND clk = '1')then-- clkIn_sync'event AND clkIn_sync = '0'
		if(inClkNegEdg = '1')then
			dataIncomingSR(0) <= dataIn;
			dataIncomingSR(7 downto 1) <= dataIncomingSR(6 downto 0);
		
		end if;
	end if;
end process;

--counter that counts the incoming bits, is reset to 0000 by the load data strobe 
process(reset_n,clk,inClkNegEdg,signalFF)--clk
begin
	if(reset_n = '0')then
		counterInByte <= (others => '0');
	
	elsif(clk'event AND clk = '1')then-- clkIn_sync'event AND clkIn_sync = '0'
		if(inClkNegEdg = '1')then
			counterInByte <= counterInByte + 1;
		
		elsif(signalFF = "11")then
			counterInByte <= (others => '0');
		
		end if;
	end if;
end process;



--flip flops that signal the "load the incoming byte" then reset the counterInByte
process(reset_n,clk,counterInByte)
begin
	if(reset_n = '0')then
		signalFF <= (others => '0');
	
	elsif(clk'event AND clk = '1')then
		if(counterInByte = byteCount)then
			signalFF(0) <= '1';
			signalFF(1) <= signalFF(0);
		else 
			signalFF(0) <= '0';
			signalFF(1) <= signalFF(0);
		end if;	
	end if;
end process;

--process loads the byte from the incoming data shift register to the byte holding register
process(reset_n,clk,signalFF)
begin
	if(reset_n = '0')then
		dataHoldingFF <= (others => '0');
	
	elsif(clk'event AND clk = '1')then
		if(signalFF = "01")then
			dataHoldingFF <= dataIncomingSR;
		
		end if;
	end if;
end process;

--outputs the received byte to LEDs to display
LEDs <= NOT dataHoldingFF;

end behavior;