

library ieee;
use ieee.std_logic_1164.all;
use ieee.std_logic_unsigned.all;

entity pb_debouncer is

port(
	 pb_in    :in std_logic;
	 pb_out   :out std_logic;
	 
	 clk_in   :in std_logic;
	 reset_n    :in std_logic 
	);

end pb_debouncer;

architecture debounce of pb_debouncer is


signal andgate_out    :std_logic;

signal counter        :std_logic_vector(3 downto 0);
signal term_cnt       :std_logic;

signal ff_out         :std_logic_vector(2 downto 0);
signal cur_out        :std_logic;

begin 
	
	-- counter 
	process(clk_in,reset_n,andgate_out,counter)
	begin		
		if(reset_n = '0')then		
			counter <= (others => '0');
		
		elsif(clk_in'event AND clk_in = '1')then
			if(andgate_out = '1')then
				counter <= (others => '0');
			
			else
				counter <= counter + 1;
			
			end if;
		end if;			
	end process;
	
	term_cnt <= '1' when(counter = "1111")else '0';

	-- process sets pb_out high on first button press and low on second button press 
	process(clk_in,reset_n,term_cnt,ff_out(2))
	begin	
		if(reset_n = '0')then
			cur_out <= '0';
	
		elsif(clk_in'event and clk_in = '1')then
		
			if(term_cnt = '1' AND ff_out(2) = '0')then
				cur_out <= NOT cur_out; 
				
			end if;
		end if;			
	end process;

	pb_out <= cur_out;

	--flip flops process
	process(clk_in,reset_n)
	begin
		
		if(reset_n = '0')then
			ff_out <= (others => '0');
			
		elsif(clk_in'event and clk_in = '1')then
--			ff_out <= ff_in;
			ff_out(0) <= pb_in;
			ff_out(2 downto 1) <= ff_out(1 downto 0);
			
		end if;		
	end process;

	-- logic that triggers the counter
	andgate_out <= '1' when(ff_out(2) = '1' AND ff_out(1) = '0') else '0';
		

end debounce;